package org.team4u.base.bean;

import org.team4u.base.message.Message;

/**
 * bean初始化完成事件
 *
 * @author jay.wu
 * @see SimpleBeanLoader
 * @see org.team4u.base.spring.SpringInitializedPublisher
 */
public class BeanInitializedEvent implements Message {

    private final Object bean;

    public BeanInitializedEvent(Object bean) {
        this.bean = bean;
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