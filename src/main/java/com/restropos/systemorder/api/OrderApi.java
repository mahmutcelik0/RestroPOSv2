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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi {
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

    @GetMapping(value = "/{businessDomain}/{userType}/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<OrderDto>> getEvents(@PathVariable String businessDomain, @PathVariable String userType, @PathVariable String userInfo) {
        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);

        // Emit a welcome message to the subscriber
        Flux<List<OrderDto>> welcomeMessage = Flux.just(orderService.getOrders()); //burada ilgili kişi ilk subscribe olduğunda db ye istek atılacak

        // Subscribe to events and emit relevant orders
        Flux<List<OrderDto>> filteredOrders = events.onErrorResume(throwable -> {
            LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
            return Flux.empty();
        }).filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo)).map(e -> List.of(e.getOrder())).onErrorResume(throwable -> {
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
        waiters.forEach(waiter -> events.onNext(new SubscribeKey(businessDomain, new SubscribeDto(UserTypes.WAITER, waiter.getEmail()), response)));
        return ResponseEntity.ok(response);
    }


}
