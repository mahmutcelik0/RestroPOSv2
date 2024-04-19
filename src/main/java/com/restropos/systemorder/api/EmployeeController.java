package com.restropos.systemorder.api;

import com.restropos.systemorder.OrderStatus;
import com.restropos.systemorder.entity.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/employees")
public class EmployeeController {
    private Order field = new Order(1L,OrderStatus.RECEIVED);
    private final Flux<Object> customer;
    private final Flux<Object> waiter;
    private final Flux<Object> kitchen;
    private final Flux<Object> cashDesk;
    private FluxSink<Object> customerSink;
    private FluxSink<Object> waiterSink;
    private FluxSink<Object> kitchenSink;
    private FluxSink<Object> cashDeskSink;

    public EmployeeController() {
        this.customer = Flux.create(sink -> {
            this.customerSink = sink;
        }).share();

        this.waiter = Flux.create(sink -> {
            this.waiterSink = sink;
        }).share();

        this.kitchen = Flux.create(sink -> {
            this.kitchenSink = sink;
        }).share();

        this.cashDesk = Flux.create(sink -> {
            this.cashDeskSink = sink;
        }).share();
    }

    @GetMapping("/{id}")
    public Mono<String> getEmployeeById(@PathVariable String id) {
        return Mono.just("123");
    }

    @PostMapping("/{status}")
    public void setId(@PathVariable String status) {
        field.setOrderStatus(OrderStatus.valueOf(status));
        // İlk kez çağrıldığında sink nesnesini başlat
        if(status.equals(OrderStatus.PREPARING.name())) customerSink.next(field);
        else if(status.equals(OrderStatus.SERVING.name())) waiterSink.next(field);
        else if(status.equals(OrderStatus.RECEIVED.name())) kitchenSink.next(field);
        else if(status.equals(OrderStatus.COMPLETED.name())) cashDeskSink.next(field);

    }

    @GetMapping(value = "/customer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> customerUpdates() {
        return customer;
    }

    @GetMapping(value = "/waiter", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> waiterUpdates() {
        return waiter;
    }

    @GetMapping(value = "/kitchen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> kitchenUpdates() {
        return kitchen;
    }

    @GetMapping(value = "/cashDesk", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> cashDeskUpdates() {
        return cashDesk;
    }

}
