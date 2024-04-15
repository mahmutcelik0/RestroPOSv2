package com.restropos.systemorder.api;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class EmployeeWebClient {

    WebClient client = WebClient.create("http://localhost:8080");

    public void consume() {
        Mono<String> employeeMono = client.get()
                .uri("/auth/employees/{id}", "1")
                .retrieve()
                .bodyToMono(String.class);

        Mono<String> updates = client.get()
                .uri("/auth/employees/updates")
                .retrieve()
                .bodyToMono(String.class);
        employeeMono.subscribe(System.out::println);
        updates.subscribe(System.out::println);

    }
    // ...
}
