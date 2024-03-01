package com.restropos;

import com.restropos.systemverify.config.TwilioConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.twilio.Twilio;

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

}
