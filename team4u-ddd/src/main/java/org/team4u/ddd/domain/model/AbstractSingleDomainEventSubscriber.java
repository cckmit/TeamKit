package org.team4u.ddd.domain.model;

import cn.hutool.core.util.ClassUtil;

/**
 * 抽象单一事件处理器
 *
 * @param <E> 事件类型
 * @author jay.wu
 */
public abstract class AbstractSingleDomainEventSubscriber<E extends DomainEvent> extends AbstractDomainEventSubscriber {

    @Override
    protected void handle(DomainEvent event) throws Throwable {
        if (!supports(event)) {
            return;
        }
        //noinspection unchecked
        handleEvent((E) event);
    }

    /**
     * 判断是否监听指定的事件类型
     */
    @Override
    protected boolean supports(DomainEvent event) {
        return typeOfEventSubscribed().isAssignableFrom(event.getClass());
    }

    /**
     * 处理事件
     *
     * @param event 要处理的事件
     */
    protected abstract void handleEvent(E event) throws Throwable;

    /**
     * 已订阅的事件类
     *
     * @return 事件类
     */
    @SuppressWarnings("unchecked")
    protected Class<E> typeOfEventSubscribed() {
        return (Class<E>) ClassUtil.getTypeArgument(this.getClass());
    }
}