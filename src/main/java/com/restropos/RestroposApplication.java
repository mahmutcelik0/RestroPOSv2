package com.restropos;

import com.restropos.systemorder.api.EmployeeWebClient;
import com.restropos.systemorder.entity.Order;
import com.restropos.systemverify.config.TwilioConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.twilio.Twilio;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableConfigurationProperties
public class RestroposApplication {
    @Autowired
    private TwilioConfig twilioConfig;

    @PostConstruct
    public void setup(){
        Twilio.init(twilioConfig.getAccountSID(),twilioConfig.getAuthToken());
    }

    public static void main(String[] args) {
        SpringApplication.run(RestroposApplication.class, args);


    }

    @EventListener(ApplicationReadyEvent.class)
    public void listenToEmployeeUpdates() {
        WebClient client = WebClient.create("http://localhost:8080");
//        Flux<Object> customer = client.get()
//                .uri("/auth/employees/customer")
//                .retrieve()
//                .bodyToFlux(Object.class);
//
//        Flux<Object> waiter = client.get()
//                .uri("/auth/employees/waiter")
//                .retrieve()
//                .bodyToFlux(Object.class);
//
//        Flux<Object> kitchen = client.get()
//                .uri("/auth/employees/kitchen")
//                .retrieve()
//                .bodyToFlux(Object.class);
//
//        Flux<Object> cashDesk = client.get()
//                .uri("/auth/employees/cashDesk")
//                .retrieve()
//                .bodyToFlux(Object.class);

//        customer.subscribe(
//                value -> System.out.println("Customer : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );
//
//        waiter.subscribe(
//                value -> System.out.println("waiter : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );
//
//        kitchen.subscribe(
//                value -> System.out.println("kitchen : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );
//
//        cashDesk.subscribe(
//                value -> System.out.println("cashDesk : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );

        Flux<String> customer = client.get()
                .uri("/auth/orders/subdomain1/CUSTOMER/5466053396")
                .retrieve()
                .bodyToFlux(String.class);

        Flux<String> customer2 = client.get()
                .uri("/auth/orders/subdomain2/CUSTOMER/5466053396")
                .retrieve()
                .bodyToFlux(String.class);

//        customer.subscribe(
//                value -> System.out.println("Customer : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );
//
//        customer2.subscribe(
//                value -> System.out.println("Customer : " + value),
//                error -> System.err.println("Hata: " + error),
//                () -> System.out.println("Akış tamamlandı")
//        );
    }

}
