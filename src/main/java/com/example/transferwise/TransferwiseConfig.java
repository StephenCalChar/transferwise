package com.example.transferwise;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TransferwiseConfig {

//    @Bean
//    public RestOperations restOperations() {
//        return new RestTemplate();
//    }
//
//    //Probably wants to be an interface.
//    @Bean
//    public AuthToken authToken() {
//        return new AuthToken();
//    }
//
//    @Bean
//    public TransferwiseApi transferwiseApi() {
//        return new TransferwiseApi();
//    }

    @Bean
    public TransferwiseClient transferwiseClient() {
        RestTemplate restTemplate = new RestTemplate();
        return new TransferwiseClient(restTemplate);
    }
}
