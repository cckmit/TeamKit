package org.team4u.ddd.infrastructure.process.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.team4u.ddd.process.FakeTimeConstrainedProcessTrackerRepository;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.strategy.FakeRetryStrategyRepository;

@ComponentScan("org.team4u.ddd.infrastructure.process.spring")
@Import(RetryBeanConfig.class)
@Configuration
public class TestRetryBeanConfig {

    @Bean
    public FakeTimeConstrainedProcessTrackerRepository trackerRepository() {
        return new FakeTimeConstrainedProcessTrackerRepository();
    }

    @Bean
    public FakeRetryStrategyRepository retryStrategyRepository() {
        return new FakeRetryStrategyRepository();
    }

    @Bean
    public TimeConstrainedProcessTrackerAppService trackerAppService(
            FakeTimeConstrainedProcessTrackerRepository trackerRepository,
            FakeRetryStrategyRepository retryStrategyRepository
    ) {
        return new TimeConstrainedProcessTrackerAppService(
                trackerRepository, retryStrategyRepository
        );
    }
}