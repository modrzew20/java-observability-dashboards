package com.github.dm.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient notificationServiceWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://notification-service:8081")
                .build();
    }
}
