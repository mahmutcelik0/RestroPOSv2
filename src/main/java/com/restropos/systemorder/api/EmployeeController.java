package com.restropos.systemorder.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/employees")
public class EmployeeController {
    private String field = "123";
    private final Flux<Object> employeeUpdateStream;
    private FluxSink<Object> sink;

    public EmployeeController() {
        this.employeeUpdateStream = Flux.create(sink -> {
            this.sink = sink;
        }).share();
    }

    @GetMapping("/{id}")
    public Mono<String> getEmployeeById(@PathVariable String id) {
        return Mono.just(field);
    }

    @PostMapping("/{id}")
    public void setId(@PathVariable String id) {
        field = id;
        // İlk kez çağrıldığında sink nesnesini başlat
        if (sink != null) {
            sink.next(field);
        }
    }

    @GetMapping(value = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> employeeUpdates() {
        return employeeUpdateStream;
    }
}
