package com.automatics.devicemanager.healthcheck.registry.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync

public class AsyncConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
	log.debug("Creating Async Task Executor");
	final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	executor.setCorePoolSize(300);
	executor.setMaxPoolSize(600);
	executor.setQueueCapacity(30);
	return executor;
    }
}
