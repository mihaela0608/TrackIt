package com.example.trackit.config;

import com.example.trackit.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {


    @Bean
    public RestClient  userRestClient() {
        return RestClient
                .builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
