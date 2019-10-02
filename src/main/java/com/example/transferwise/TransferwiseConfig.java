package com.example.transferwise;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TransferwiseConfig {
    @Bean
    public TransferwiseClient transferwiseClient() {
        RestTemplate restTemplate = new RestTemplate();
        return new TransferwiseClient(restTemplate);
    }
}
