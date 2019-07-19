package org.team4u.kit.core.event.api;

/**
 * 事件总线接口。当接收到领域事件时调用合适的事件处理器处理这些事件。
 *
 * @author jay.wu
 */
public interface EventBus {

    /**
     * 注册事件处理器
     *
     * @param handlers 要注册的事件处理器
     */
    void register(EventListener... handlers);

    /**
     * 卸载事件处理器
     *
     * @param handlers 要卸载的事件处理器
     */
    void unregister(EventListener... handlers);

    /**
     * 接收领域事件
     *
     * @param event 要接收的领域事件
     */
    void post(Event event);

}
