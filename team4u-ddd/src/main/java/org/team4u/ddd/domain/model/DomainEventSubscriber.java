package org.team4u.ddd.domain.model;

public interface DomainEventSubscriber<T extends DomainEvent> {

    /**
     * 处理事件
     *
     * @param event 要处理的事件
     */
    void onEvent(T event);
}