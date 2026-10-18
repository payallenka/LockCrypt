package com.lockcrypt.vaadinframework.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    public static final String INTERNAL_TOKEN_HEADER = "X-Internal-Token";

    // @LoadBalanced lets us call services by their Eureka name, e.g. http://MongoDBDatabase/userData
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     @Value("${lockcrypt.internal-token}") String internalToken) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5))
                .defaultHeader(INTERNAL_TOKEN_HEADER, internalToken)
                .build();
    }
}
