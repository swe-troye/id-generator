package com.swetroye.idgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swetroye.idgenerator.impl.IdGeneratorImpl;

@Configuration
public class AppConfig {

    @Bean
    public IdGeneratorImpl idGeneratorImpl() {
        return new IdGeneratorImpl();
    }
}
