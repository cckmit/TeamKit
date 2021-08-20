package org.team4u.base.bean.event;

import org.team4u.base.message.AbstractMessageSubscriber;

/**
 * BeanInitializedEvent抽象监听器
 *
 * @author jay.wu
 */
public abstract class AbstractBeanInitializedEventSubscriber extends AbstractMessageSubscriber<BeanInitializedEvent> {

    @Override
    protected boolean supports(Object message) {
        if (!super.supports(message)) {
            return false;
        }

        return getBeanType().isAssignableFrom(((BeanInitializedEvent) message).getBean().getClass());
    }

    /**
     * 获取指定监听的bean类型
     */
    public abstract Class<?> getBeanType();
}