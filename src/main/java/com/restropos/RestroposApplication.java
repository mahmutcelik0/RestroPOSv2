package com.restropos;

import com.restropos.systemorder.dto.OrderDto;
import com.restropos.systemverify.config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
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

//    @EventListener(ApplicationReadyEvent.class)
//    public void listenToEmployeeUpdates() {
//        WebClient client = WebClient.create("http://localhost:8080");
//
//        Flux<OrderDto> customer = client.get()
//                .uri("/auth/orders/subdomain1/CUSTOMER/905511223122")
//                .retrieve()
//                .bodyToFlux(OrderDto.class);
//
////        Flux<String> customer2 = client.get()
////                .uri("/auth/orders/subdomain2/CUSTOMER/5466053396")
////                .retrieve()
////                .bodyToFlux(String.class);
//
//        customer.subscribe(System.out::println);
////        customer2.subscribe(System.out::println);
//    }
}
