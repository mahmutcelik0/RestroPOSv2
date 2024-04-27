package com.restropos.systemcore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig  {
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        return new OpenEntityManagerInViewInterceptor();
    }
}
