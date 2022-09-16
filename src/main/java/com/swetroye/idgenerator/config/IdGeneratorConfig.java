package com.swetroye.idgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.swetroye.idgenerator.impl.IdGeneratorImpl;
import com.swetroye.idgenerator.impl.WorkerManagerImpl;

@Configuration
public class IdGeneratorConfig {

    @Bean
    public IdGeneratorImpl idGeneratorImpl() {

        return new IdGeneratorImpl();
    }

    @Bean
    public WorkerManagerImpl workerManagerImpl() {

        return new WorkerManagerImpl();
    }

}
