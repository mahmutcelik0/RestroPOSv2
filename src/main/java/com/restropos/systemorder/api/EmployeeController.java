package com.restropos.systemorder.api;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/employees")
public class EmployeeController {
    @GetMapping("/{id}")
    public Mono<String> getEmployeeById(@PathVariable String id) {
        return Mono.just("123");
    }

    @PostMapping("/{status}")
    public void setId(@PathVariable String status) {
//        field.setOrderStatus(OrderStatus.valueOf(status));
//         İlk kez çağrıldığında sink nesnesini başlat
//        if(status.equals(OrderStatus.PREPARING.name())) customerSink.next(field);
//        else if(status.equals(OrderStatus.SERVING.name())) waiterSink.next(field);
//        else if(status.equals(OrderStatus.RECEIVED.name())) kitchenSink.next(field);
//        else if(status.equals(OrderStatus.COMPLETED.name())) cashDeskSink.next(field);

    }


}
