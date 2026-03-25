package org.ewsd.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;


import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
@Configuration
@RequiredArgsConstructor
public class AsyncConfig {
    @Primary
    @Bean(name = "applicationTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual()
                .name("async-virtual-", 0)
                .factory();
        return new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(virtualThreadFactory));
    }
}
