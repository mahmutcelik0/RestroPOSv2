package com.restropos.systemorder.api;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemorder.populator.OrderDtoPopulator;
import com.restropos.systemorder.service.FirebaseService;
import com.restropos.systemorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi  implements WebMvcConfigurer{
    @Autowired
    private OrderService orderService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private OrderDtoPopulator orderDtoPopulator;


//    @Transactional
//    @GetMapping(value = "/{businessDomain}/CUSTOMER/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<List<OrderDto>> getEventsForCustomer(@PathVariable String businessDomain, @PathVariable String userInfo) {
//        String userType = UserTypes.CUSTOMER.name();
//        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);
//
//        // Emit a welcome message to the subscriber
//        Flux<List<OrderDto>> welcomeMessage = null;
//        if (userType.equalsIgnoreCase(UserTypes.CUSTOMER.getName())) {
//            welcomeMessage = Flux.just(orderService.getCustomerActiveOrders(userInfo, businessDomain));
//        } else if (userType.equalsIgnoreCase(UserTypes.WAITER.name())) {
//            welcomeMessage = Flux.just(orderService.getActiveOrders(businessDomain));
//        }
//
//        // Subscribe to events and emit relevant orders
//        Flux<List<OrderDto>> filteredOrders = events.onErrorResume(throwable -> {
//            LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
//            return Flux.empty();
//        }).filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo)).map(SubscribeKey::getOrder).onErrorResume(throwable -> {
//            LogUtil.printLog("Error occurred while mapping orders: " + throwable.getMessage(), OrderApi.class);
//            return Flux.empty();
//        });
//
//        // Concatenate the welcome message and filtered orders
//        return Flux.concat(welcomeMessage, filteredOrders).share();
//    }

//    @GetMapping(value = "/{businessDomain}/WAITER/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<List<OrderDto>> getEventsForWaiter(@PathVariable String businessDomain, @PathVariable String userInfo) {
//        String userType = UserTypes.WAITER.name();
//        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);
//
//        // Emit a welcome message to the subscriber
//        Flux<List<OrderDto>> welcomeMessage = null;
//        if (userType.equalsIgnoreCase(UserTypes.CUSTOMER.getName())) {
//            welcomeMessage = Flux.just(orderService.getCustomerActiveOrders(userInfo, businessDomain));
//        } else if (userType.equalsIgnoreCase(UserTypes.WAITER.name())) {
//            welcomeMessage = Flux.just(orderService.getActiveOrders(businessDomain));
//        }
//
//        // Subscribe to events and emit relevant orders
//        Flux<List<OrderDto>> filteredOrders = events.onErrorResume(throwable -> {
//            LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
//            return Flux.empty();
//        }).filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo)).map(SubscribeKey::getOrder).onErrorResume(throwable -> {
//            LogUtil.printLog("Error occurred while mapping orders: " + throwable.getMessage(), OrderApi.class);
//            return Flux.empty();
//        });
//
//        // Concatenate the welcome message and filtered orders
//        return Flux.concat(welcomeMessage, filteredOrders).share();
//    }



    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) throws NotFoundException {
        LogUtil.printLog("EVENT TRIGGERED:" + orderDto, OrderApi.class);
        Order order = orderService.createOrder(orderDto);
        OrderDto response = orderDtoPopulator.populate(order);
        firebaseService.save(response);
        return ResponseEntity.ok(CustomResponseMessage.ORDER_CREATED);
    }

    @DeleteMapping
    public void deleteOrder(@RequestBody OrderDto orderDto){
        firebaseService.delete(orderService.getOrder(1L));
    }


}
