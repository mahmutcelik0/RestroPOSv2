package com.restropos.systemorder.api;

import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemorder.dto.SubscribeDto;
import com.restropos.systemorder.dto.SubscribeKey;
import com.restropos.systemorder.service.OrderService;
import com.restropos.systemshop.constants.UserTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/auth/orders")
public class OrderApi {
    @Autowired
    private OrderService orderService;

    private EmitterProcessor<SubscribeKey> events = EmitterProcessor.create();

    @GetMapping(value = "/{businessDomain}/{userType}/{userInfo}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderDto> getEvents(@PathVariable String businessDomain,@PathVariable String userType,@PathVariable String userInfo) {
//        events.onNext(new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"5466053396"),"FirstOrder1"));
        return events.share()
                .filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo))
                .map(SubscribeKey::getOrder);
    }

    @GetMapping(value = "/qq", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getEventsEx() {
//        events.onNext(new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"5466053396"),"FirstOrder1"));
        return events.share()
//                .filter(event -> event.getBusinessDomain().equals(businessDomain) && event.getSubscribeDto().getUserType().name().equalsIgnoreCase(userType) && event.getSubscribeDto().getUserInfo().equals(userInfo))
                .map(SubscribeKey::getBusinessDomain);
    }

    /*
    @PostMapping
    public void trigger(){
        events.onNext(new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"5466053396"),"FirstOrder1"));
//        events.onNext(new SubscribeKey("subdomain2",new SubscribeDto(UserTypes.CUSTOMER,"5466053396"),"FirstOrder2"));
    }
    * */


    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        orderService.createOrder(orderDto);
        events.onNext(new SubscribeKey("subdomain1",new SubscribeDto(UserTypes.CUSTOMER,"5466053396"),orderDto));
        return ResponseEntity.ok(orderDto);
    }



//    @PostMapping
//    public ResponseEntity<String> subscribeUser(@RequestBody SubscribeDto subscribeDto){
////        orderService.subscribeUser(subscribeDto);
//        return ResponseEntity.ok("Subscribed");
//    }
//
//    @PostMapping("/subscribe/{businessDomain}/customer")
//    public Mono<String> subscribeCustomer(@PathVariable String businessDomain){//, @RequestBody SubscribeDto subscribeDto
//        SubscribeDto subscribeDto1 = new SubscribeDto(UserTypes.CUSTOMER,"5466053396");
//    }
//
//    @GetMapping("/stream/{businessDomain}/customer")
//    public Flux<ServerSentEvent<String>> streamMessages(@PathVariable String businessDomain) {//,@RequestBody SubscribeDto subscribeDto
//        SubscribeDto subscribeDto1 = new SubscribeDto(UserTypes.CUSTOMER,"5466053396");
//    }
//
//    @PostMapping("/send/{userId}")
//    public Mono<String> sendMessage(@PathVariable String businessDomain, @RequestBody SubscribeDto subscribeDto) {
//    }

//    @PostMapping("/subscribe/{businessDomain}/waiter")
//    public ResponseEntity<String> subscribeWaiter(@PathVariable String businessDomain, @RequestBody SubscribeDto subscribeDto){
//        return orderService.subscribeWaiter(businessDomain,subscribeDto);
//    }
//
//    @PostMapping("/subscribe/{businessDomain}/kitchen")
//    public ResponseEntity<String> subscribeKitchen(@PathVariable String businessDomain, @RequestBody SubscribeDto subscribeDto){
//        return orderService.subscribeKitchen(businessDomain,subscribeDto);
//    }
//
//    @PostMapping("/subscribe/{businessDomain}/cashDesk")
//    public ResponseEntity<String> subscribeCashDesk(@PathVariable String businessDomain, @RequestBody SubscribeDto subscribeDto){
//        return orderService.subscribeCashDesk(businessDomain,subscribeDto);
//    }
}
