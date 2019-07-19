package org.team4u.kit.core.event.impl;


import org.team4u.kit.core.event.api.EventBus;
import org.team4u.kit.core.event.api.EventListener;

/**
 * 事件处理器注册器
 *
 * @author jay.wu
 */
public class SimpleEventListenerRegister {

    private EventBus eventBus;

    private EventListener[] listeners;

    public SimpleEventListenerRegister(EventBus eventBus, EventListener[] listeners) {
        this.eventBus = eventBus;
        this.listeners = listeners;
    }

    /**
     * 将所有事件处理器注册到事件总线
     */
    public void register() {
        for (EventListener listener : listeners) {
            eventBus.register(listener);
        }
    }
}