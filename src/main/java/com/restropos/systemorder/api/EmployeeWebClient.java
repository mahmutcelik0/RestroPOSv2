package com.restropos.systemorder.api;

import com.restropos.systemorder.dto.OrderDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmployeeWebClient {

    WebClient client = WebClient.create("http://localhost:8080");

    public void consume() {
//        Mono<String> employeeMono = client.get()
//                .uri("/auth/employees/{id}", "1")
//                .retrieve()
//                .bodyToMono(String.class);

        Flux<OrderDto> updates = client.get()
                .uri("/auth/orders/subdomain1/CUSTOMER/5466053396")
                .retrieve()
                .bodyToFlux(OrderDto.class);
//        employeeMono.subscribe(System.out::println);
        updates.subscribe(System.out::println);

        Flux<String> aa = client.get()
                .uri("/auth/orders/qq")
                .retrieve()
                .bodyToFlux(String.class);
//        employeeMono.subscribe(System.out::println);
        aa.subscribe(System.out::println);

    }
    // ...
}
