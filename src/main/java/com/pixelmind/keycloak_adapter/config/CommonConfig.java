package com.pixelmind.keycloak_adapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CommonConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
}
