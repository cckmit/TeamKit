package org.team4u.kit.core.event.api;

/**
 * 事件监听器
 *
 * @author jay.wu
 */
public interface EventListener<T extends Event> {

    /**
     * 处理事件
     *
     * @param event 要处理的事件
     */
    void onEvent(T event);

}