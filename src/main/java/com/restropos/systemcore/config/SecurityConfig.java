package com.restropos.systemcore.config;

import com.restropos.systemcore.filter.JwtAuthenticationFilter;
import com.restropos.systemshop.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private WorkspaceService workspaceService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(getSubdomains());
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    corsConfiguration.setAllowCredentials(true); //user credential alabilmek için true ya setlendi
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setExposedHeaders(List.of("Authorization")); //JWT token ı backend ten UI a yollamamızı sağlayacak
                    corsConfiguration.setMaxAge(3600L);
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/**").authenticated() //.hasAnyRole("USER","ADMIN")
                        .requestMatchers("/auth/**","/swagger-ui/**","/swagger-resources/*","/v3/api-docs/**").permitAll())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public List<String> getSubdomains(){
        List<String> subdomainOrigins = new ArrayList<>();
        workspaceService.getAllWorkspaces().forEach(e -> {
            subdomainOrigins.add(generateSubdomainOriginWithSsl(e));
            subdomainOrigins.add(generateSubdomainOriginWithoutSslForLocal(e));
        });

        subdomainOrigins.add("http://subdomain1.localhost:5173"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("http://subdomain2.localhost:5173"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("http://subdomain3.localhost:5173"); //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("https://subdomain1.restropos.software");  //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("https://subdomain2.restropos.software");  //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("https://subdomain3.restropos.software");  //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("http://localhost:5173");  //todo SEDAT OZEL ISTEK ILERDE SILINMESI SART
        subdomainOrigins.add("http://**.localhost:5173");
        subdomainOrigins.add("https://**.restropos.software");
        subdomainOrigins.add("http://**.localhost:5173");
        subdomainOrigins.add("https://restropos.software");
        return subdomainOrigins;
    }

    private String generateSubdomainOriginWithSsl(String e) {
        return "https://"+e+"restropos.software";
    }


    private String generateSubdomainOriginWithoutSslForLocal(String e){
        return "http://"+e+".localhost:5173";
    }


}
