package org.team4u.ddd.event;

import org.team4u.ddd.domain.model.DomainEvent;

import java.io.Closeable;
import java.util.List;

/**
 * 事件资源库
 *
 * @author jay.wu
 */
public interface EventStore extends Closeable {

    /**
     * 获取已存储的事件集合
     *
     * @param lowStoredEventId  最小存储的事件标识（包含）
     * @param highStoredEventId 最大存储的事件标识（包含）
     */
    List<StoredEvent> allStoredEventsBetween(long lowStoredEventId, long highStoredEventId);

    /**
     * 获取已存储的事件集合
     *
     * @param storedEventId 起始存储的事件标识（不包含）
     */
    List<StoredEvent> allStoredEventsSince(long storedEventId);

    /**
     * 添加事件
     */
    StoredEvent append(DomainEvent domainEvent);

    /**
     * 计算已存储事件数量
     */
    long countStoredEvents();

    /**
     * 删除已存储事件
     *
     * @param storedEventId 起始存储的事件标识（不包含）
     */
    void removeStoredEventsBefore(long storedEventId);
}