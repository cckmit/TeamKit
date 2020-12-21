package org.team4u.ddd.infrastructure.persistence.memory;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.id.NumberIdentityFactory;
import org.team4u.base.id.SnowflakeIdentityFactory;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.event.EventSerializer;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.team4u.ddd.infrastructure.persistence.mybatis.StoredEventEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于内存的事件资源库
 *
 * @author jay.wu
 */
public class InMemoryEventStore implements EventStore {

    private final NumberIdentityFactory identityFactory = new SnowflakeIdentityFactory(1, 1);

    private List<StoredEvent> storedEvents = new ArrayList<>();

    @Override
    public List<StoredEvent> allStoredEventsOf(String domainId) {
        return storedEvents.stream()
                .filter(it -> StrUtil.equals(it.domainId(), domainId))
                .collect(Collectors.toList());
    }

    @Override
    public List<StoredEvent> allStoredEventsBetween(long lowStoredEventId, long highStoredEventId) {
        return storedEvents.stream()
                .filter(it -> it.eventId() >= lowStoredEventId)
                .filter(it -> it.eventId() <= highStoredEventId)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoredEvent> allStoredEventsSince(long storedEventId) {
        return storedEvents.stream()
                .filter(it -> it.eventId() > storedEventId)
                .collect(Collectors.toList());
    }

    @Override
    public StoredEvent append(DomainEvent domainEvent) {
        StoredEvent storedEvent = new StoredEvent(
                identityFactory.create().longValue(),
                domainEvent.getDomainId(),
                domainEvent.getClass().getName(),
                domainEvent.getOccurredOn(),
                EventSerializer.instance().serialize(domainEvent)
        );

        storedEvents.add(storedEvent);
        return storedEvent;
    }

    @Override
    public void close() {
    }

    @Override
    public long countStoredEvents() {
        return storedEvents.size();
    }

    @Override
    public void removeStoredEventsBefore(long storedEventId) {
        storedEvents = allStoredEventsSince(storedEventId);
    }

    protected StoredEventEntity storedEventEntityFrom(StoredEvent event) {
        StoredEventEntity entity = new StoredEventEntity();
        entity.setEventId(event.eventId());
        entity.setDomainId(event.domainId());
        entity.setTypeName(event.typeName());
        entity.setEventBody(event.eventBody());
        entity.setOccurredOn(event.occurredOn());
        entity.setCreateTime(event.occurredOn());
        entity.setUpdateTime(event.occurredOn());
        return entity;
    }
}