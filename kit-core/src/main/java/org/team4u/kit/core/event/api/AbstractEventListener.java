package org.team4u.kit.core.event.api;

import java.lang.reflect.ParameterizedType;

/**
 * 抽象事件处理器
 * <p>
 * 事件总线在接收到一个领域事件时，先调用supports(event)方法判断当前监听器是否支持该事件，
 * 如果支持则调用onEvent()方法处理该事件
 *
 * @param <T> 事件类型
 * @author jay.wu
 */
public abstract class AbstractEventListener<T extends Event> implements EventListener<T> {

    @Override
    public void onEvent(T event) {
        if (!supports(event)) {
            return;
        }
        handle(event);
    }

    /**
     * 判断是否监听指定的事件类型
     */
    public boolean supports(Event event) {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<?> eventClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return eventClass.isAssignableFrom(event.getClass());
    }

    /**
     * 处理事件
     *
     * @param event 要处理的事件
     */
    protected abstract void handle(T event);
}