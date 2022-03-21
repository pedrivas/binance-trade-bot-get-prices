package com.prices.binance.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean(name="objectMapper")
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }
}
