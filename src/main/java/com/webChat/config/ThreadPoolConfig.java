package com.webChat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liuhua
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${spring.task.execution.pool.core-size}")
    private int corePoolSize;
    @Value("${spring.task.execution.pool.max-size}")
    private int maxPoolSize;
    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;
    @Value("${spring.task.execution.thread-name-prefix}")
    private String threadNamePrefix;

    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();



        return executor;
    }

}
