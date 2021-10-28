package org.team4u.base.bean.event;

import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.Message;

/**
 * bean初始化完成事件
 *
 * @author jay.wu
 * @see BeanProviders
 * @see org.team4u.base.spring.SpringInitializedPublisher
 * @see AbstractBeanInitializedEventSubscriber
 */
public class BeanInitializedEvent implements Message {

    private final String beanName;
    private final Object bean;

    public BeanInitializedEvent(String beanName, Object bean) {
        this.beanName = beanName;
        this.bean = bean;
    }

    public String getBeanName() {
        return beanName;
    }

    public <T> T getBean() {
        //noinspection unchecked
        return (T) bean;
    }

    @Override
    public String toString() {
        return bean.getClass().getName();
    }
}