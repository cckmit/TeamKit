package org.team4u.kit.core.event.api;

/**
 * 事件存储
 *
 * @author jay.wu
 */
public interface EventStore {

    void store(Event event);
}
