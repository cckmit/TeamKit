package org.team4u.base.bean.event;

import org.team4u.base.message.AbstractMessageSubscriber;

/**
 * BeanInitializedEvent抽象监听器
 *
 * @author jay.wu
 */
public abstract class AbstractBeanInitializedEventSubscriber extends AbstractMessageSubscriber<BeanInitializedEvent> {

    /**
     * 指定bean类型
     */
    private final Class<?> beanType;

    protected AbstractBeanInitializedEventSubscriber(Class<?> beanType) {
        this.beanType = beanType;
    }

    @Override
    protected boolean supports(Object message) {
        if (!super.supports(message)) {
            return false;
        }

        return beanType.isAssignableFrom(((BeanInitializedEvent) message).getBean().getClass());
    }
}