package com.restropos;

import com.restropos.systemverify.config.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

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
//        List<OrderDto> list;
//        Flux<List<OrderDto>> customer = client.get()
//                .uri("/auth/orders/subdomain1/CUSTOMER/905511223122")
//                .retrieve()
//                .bodyToFlux(new ParameterizedTypeReference<List<OrderDto>>() {});
//
//        Flux<List<OrderDto>> waiter = client.get()
//                .uri("/auth/orders/subdomain1/WAITER/garson@gmail.com")
//                .retrieve()
//                .bodyToFlux(new ParameterizedTypeReference<List<OrderDto>>() {});
//
//        customer.subscribe(orderDtos ->{
//            orderDtos.forEach(e->{
//                System.out.println("ACTIVE CUSTOMER ORDERS:"+e.getId());
//            });
//        });
//        waiter.subscribe(orderDtos ->{
//            orderDtos.forEach(e->{
//                System.out.println("ACTIVE VVAITER ORDERS:"+e.getId());
//            });
//        });
//    }
}
