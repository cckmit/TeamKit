package org.team4u.ddd.infrastructure.process.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.method.interceptor.RetryInterceptor;

/**
 * 重试bean定义
 *
 * @author jay.wu
 */
@EnableAspectJAutoProxy
@Configuration
public class RetryBeanConfig {

    @Bean
    public RetryInterceptor retryInterceptor(TimeConstrainedProcessTrackerAppService trackerAppService) {
        return new RetryInterceptor(trackerAppService);
    }

    @Bean
    public SpringRetryableMethodTimedOutEventSubscriber springRetryableMethodTimedOutEventSubscriber(
            TimeConstrainedProcessTrackerAppService trackerAppService) {
        return new SpringRetryableMethodTimedOutEventSubscriber(trackerAppService);
    }

    @Bean
    public SpringAspectRetryableMethodInterceptor retryableMethodInterceptor(
            RetryInterceptor retryInterceptor) {
        return new SpringAspectRetryableMethodInterceptor(retryInterceptor);
    }
}