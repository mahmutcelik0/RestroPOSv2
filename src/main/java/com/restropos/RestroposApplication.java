package com.restropos;

import com.restropos.systemorder.api.EmployeeWebClient;
import com.restropos.systemverify.config.TwilioConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.twilio.Twilio;
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

    @EventListener(ApplicationReadyEvent.class)
    public void listenToEmployeeUpdates() {
        WebClient client = WebClient.create("http://localhost:8080");
        Flux<Object> employeeUpdates = client.get()
                .uri("/auth/employees/updates")
                .retrieve()
                .bodyToFlux(Object.class);

        employeeUpdates.subscribe(
                value -> System.out.println("Yeni değer: " + value),
                error -> System.err.println("Hata: " + error),
                () -> System.out.println("Akış tamamlandı")
        );
    }

}
