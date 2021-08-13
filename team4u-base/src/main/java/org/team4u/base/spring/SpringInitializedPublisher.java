package org.team4u.base.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.team4u.base.bean.ApplicationInitializedEvent;
import org.team4u.base.message.MessagePublisher;
import org.team4u.base.message.MessageSubscriber;

import java.util.List;

/**
 * spring相关初始化事件发布者
 *
 * @author jay.wu
 */
@Component
public class SpringInitializedPublisher implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    @Autowired(required = false)
    public SpringInitializedPublisher(List<MessageSubscriber<?>> subscribers) {
        MessagePublisher.instance().subscribe(subscribers);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        MessagePublisher.instance().publish(bean);

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }

        MessagePublisher.instance().publish(new ApplicationInitializedEvent());
    }
}