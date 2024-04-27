package com.restropos.systemorder.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.dto.SubscribeDto;
import com.restropos.systemorder.dto.SubscribeKey;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemorder.populator.OrderDtoPopulator;
import com.restropos.systemorder.service.OrderService;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.SystemUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi  implements WebMvcConfigurer{
    @Autowired
    private OrderService orderService;

    private EmitterProcessor<SubscribeKey> events = EmitterProcessor.create();

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private OrderDtoPopulator orderDtoPopulator;


//    @GetMapping(value = "/{businessDomain}/{userType}/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<OrderDto> getEvents(@PathVariable String businessDomain,@PathVariable String userType,@PathVariable String userInfo) {
//        LogUtil.printLog("CONNECTED TO:"+businessDomain+"-"+userType+"-"+userInfo, OrderApi.class);
//        return events
//                .doOnSubscribe(subscription ->{
//                   // orderService.getOrders().forEach(e->{
//                    //                        new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"905511223122"),e);
//                    //                    });
//                    new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"905511223122"),orderService.getOrders().get(0));
//                })
//                .share()
//                .filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo))
//                .map(SubscribeKey::getOrder);
//    }

    @Transactional
    @GetMapping(value = "/{businessDomain}/CUSTOMER/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<OrderDto>> getEventsForCustomer(@PathVariable String businessDomain, @PathVariable String userInfo) {
        String userType = UserTypes.CUSTOMER.name();
        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);

        // Emit a welcome message to the subscriber
        Flux<List<OrderDto>> welcomeMessage = null;
        if (userType.equalsIgnoreCase(UserTypes.CUSTOMER.getName())) {
            welcomeMessage = Flux.just(orderService.getCustomerActiveOrders(userInfo, businessDomain));
        } else if (userType.equalsIgnoreCase(UserTypes.WAITER.name())) {
            welcomeMessage = Flux.just(orderService.getActiveOrders(businessDomain));
        }

        // Subscribe to events and emit relevant orders
        Flux<List<OrderDto>> filteredOrders = events.onErrorResume(throwable -> {
            LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
            return Flux.empty();
        }).filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo)).map(SubscribeKey::getOrder).onErrorResume(throwable -> {
            LogUtil.printLog("Error occurred while mapping orders: " + throwable.getMessage(), OrderApi.class);
            return Flux.empty();
        });

        // Concatenate the welcome message and filtered orders
        return Flux.concat(welcomeMessage, filteredOrders).share();
    }

    @GetMapping(value = "/{businessDomain}/WAITER/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<OrderDto>> getEventsForWaiter(@PathVariable String businessDomain, @PathVariable String userInfo) {
        String userType = UserTypes.WAITER.name();
        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);

        // Emit a welcome message to the subscriber
        Flux<List<OrderDto>> welcomeMessage = null;
        if (userType.equalsIgnoreCase(UserTypes.CUSTOMER.getName())) {
            welcomeMessage = Flux.just(orderService.getCustomerActiveOrders(userInfo, businessDomain));
        } else if (userType.equalsIgnoreCase(UserTypes.WAITER.name())) {
            welcomeMessage = Flux.just(orderService.getActiveOrders(businessDomain));
        }

        // Subscribe to events and emit relevant orders
        Flux<List<OrderDto>> filteredOrders = events.onErrorResume(throwable -> {
            LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
            return Flux.empty();
        }).filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo)).map(SubscribeKey::getOrder).onErrorResume(throwable -> {
            LogUtil.printLog("Error occurred while mapping orders: " + throwable.getMessage(), OrderApi.class);
            return Flux.empty();
        });

        // Concatenate the welcome message and filtered orders
        return Flux.concat(welcomeMessage, filteredOrders).share();
    }



    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, @RequestParam String businessDomain) throws NotFoundException {
        LogUtil.printLog("EVENT TRIGGERED:" + orderDto, OrderApi.class);
        Order order = orderService.createOrder(orderDto);
        List<SystemUser> waiters = systemUserService.getAllWaiters();
        OrderDto response = orderDtoPopulator.populate(order);
        List<OrderDto> activeOrders = orderService.getActiveOrders(businessDomain);
        List<OrderDto> customerActiveOrders = orderService.getCustomerActiveOrders(orderDto.getCustomerDto().getPhoneNumber(), businessDomain);

        waiters.forEach(waiter -> events.tryEmitNext(new SubscribeKey(businessDomain, new SubscribeDto(UserTypes.WAITER, waiter.getEmail()), activeOrders)));
        events.tryEmitNext(new SubscribeKey(businessDomain, new SubscribeDto(UserTypes.CUSTOMER, orderDto.getCustomerDto().getPhoneNumber()), customerActiveOrders));

        return ResponseEntity.ok(response);
    }

    @Autowired
    private OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor);
    }


}
