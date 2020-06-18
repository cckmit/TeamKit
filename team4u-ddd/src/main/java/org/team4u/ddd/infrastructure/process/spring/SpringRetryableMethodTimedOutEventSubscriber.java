package org.team4u.ddd.infrastructure.process.spring;

import org.team4u.ddd.process.TimeConstrainedProcessTrackerAppService;
import org.team4u.ddd.process.retry.method.interceptor.AbstractRetryableMethodTimedOutEventSubscriber;
import org.team4u.ddd.process.retry.method.interceptor.RetryContextReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 基于spring可重试超时事件订阅者
 *
 * @author jay.wu
 */
public class SpringRetryableMethodTimedOutEventSubscriber extends AbstractRetryableMethodTimedOutEventSubscriber
        implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public SpringRetryableMethodTimedOutEventSubscriber(TimeConstrainedProcessTrackerAppService trackerAppService) {
        super(trackerAppService);
    }

    @Override
    protected Object retryInvoker(RetryContextReader contextReader) {
        return applicationContext.getBean(contextReader.invokerId());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}