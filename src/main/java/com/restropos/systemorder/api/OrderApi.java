package com.restropos.systemorder.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemcore.utils.SinkProcessor;
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
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.management.Notification;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    // ExecutorService tanımla
//    private final ExecutorService executorService = Executors.newCachedThreadPool();
//
//    @GetMapping(value = "/{businessDomain}/{userType}/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<List<OrderDto>> getEvents(@PathVariable String businessDomain, @PathVariable String userType, @PathVariable String userInfo) {
//        LogUtil.printLog("CONNECTED TO:" + businessDomain + "-" + userType + "-" + userInfo, OrderApi.class);
//
//        // Yeni bir thread'de Flux oluştur
//        return Flux.defer(() -> {
//                    // Emit a welcome message to the subscriber
//                    Flux<List<OrderDto>> welcomeMessage = null;
//                    if (userType.equalsIgnoreCase(UserTypes.CUSTOMER.getName())) {
//                        welcomeMessage = Flux.just(orderService.getCustomerActiveOrders(userInfo, businessDomain));
//                    } else if (userType.equalsIgnoreCase(UserTypes.WAITER.name())) {
//                        welcomeMessage = Flux.just(orderService.getActiveOrders(businessDomain));
//                    }
//
//                    // Subscribe to events and emit relevant orders
//                    Flux<List<OrderDto>> filteredOrders = events
//                            .onErrorResume(throwable -> {
//                                LogUtil.printLog("Error occurred while processing events: " + throwable.getMessage(), OrderApi.class);
//                                return Flux.empty();
//                            })
//                            .filter(event -> event.getBusinessDomain().equals(businessDomain) &&
//                                    event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) &&
//                                    event.getSubscribeDto().getUserInfo().equals(userInfo))
//                            .map(SubscribeKey::getOrder)
//                            .onErrorResume(throwable -> {
//                                LogUtil.printLog("Error occurred while mapping orders: " + throwable.getMessage(), OrderApi.class);
//                                return Flux.empty();
//                            });
//
//                    // Concatenate the welcome message and filtered orders
//                    return Flux.concat(welcomeMessage, filteredOrders);
//                }).subscribeOn(Schedulers.fromExecutor(executorService))
////                .doFinally(signalType -> {
////                    // Flux tamamlandığında veya hata aldığında executorService'i kapat
////                    executorService.shutdown();
////                    LogUtil.printLog("Thread shut down for user: " + userInfo, OrderApi.class);
////                })
//                .share(); // Yeni thread'e geç
//    }
//

//
//    @PostMapping
//    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, @RequestParam String businessDomain) throws NotFoundException {
//        LogUtil.printLog("EVENT TRIGGERED:" + orderDto, OrderApi.class);
//        Order order = orderService.createOrder(orderDto);
//        List<SystemUser> waiters = systemUserService.getAllWaiters();
//        OrderDto response = orderDtoPopulator.populate(order);
//        List<OrderDto> activeOrders = orderService.getActiveOrders(businessDomain);
//        List<OrderDto> customerActiveOrders = orderService.getCustomerActiveOrders(orderDto.getCustomerDto().getPhoneNumber(), businessDomain);
//
//        waiters.forEach(waiter -> events.onNext(new SubscribeKey(businessDomain, new SubscribeDto(UserTypes.WAITER, waiter.getEmail()), activeOrders)));
//        events.onNext(new SubscribeKey(businessDomain, new SubscribeDto(UserTypes.CUSTOMER, orderDto.getCustomerDto().getPhoneNumber()), customerActiveOrders));
//
//        return ResponseEntity.ok(response);
//    }

    private static final SinkProcessor<OrderDto> notificationSinkProcessor = new SinkProcessor<>();

    @GetMapping(value = "/{businessDomain}/{userType}/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<List<OrderDto>>> getOrder(@PathVariable String businessDomain, @PathVariable String userType, @PathVariable String userInfo) throws NotFoundException {
        return notificationSinkProcessor.flux().map(orderDto1 -> ServerSentEvent.builder(orderService.getCustomerActiveOrders(userInfo, businessDomain))
                .retry(Duration.ofSeconds(100)).build());
//
//        LogUtil.printLog("EVENT TRIGGERED:" + orderDto, OrderApi.class);
//        Order order = orderService.createOrder(orderDto);
//        OrderDto response = orderDtoPopulator.populate(order);
//        return Mono.just(response)
//                .doOnNext(notificationSinkProcessor::add)
//                .doOnError(throwable -> LogUtil.printLog("Error on saving notification, ", OrderApi.class));
    }

    @PostMapping(path = "/streams")
    public void stream(@RequestBody OrderDto orderDto, @RequestParam String businessDomain) throws NotFoundException {
        LogUtil.printLog("EVENT TRIGGERED:" + orderDto, OrderApi.class);
        Order order = orderService.createOrder(orderDto);
//        List<SystemUser> waiters = systemUserService.getAllWaiters();
        OrderDto response = orderDtoPopulator.populate(order);
        notificationSinkProcessor.add(response);
    }
}
