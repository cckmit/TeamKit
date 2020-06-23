package org.team4u.ddd.infrastructure.persistence.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.event.EventSerializer;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.core.error.IdempotentException;
import org.team4u.core.id.IdentityFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MybatisEventStore implements EventStore {

    private final EventStoreMapper mapper;
    private final IdentityFactory<Long> identityFactory;

    public MybatisEventStore(EventStoreMapper mapper,
                             IdentityFactory<Long> identityFactory) {

        this.mapper = mapper;
        this.identityFactory = identityFactory;
    }

    @Override
    public List<StoredEvent> allStoredEventsBetween(long lowStoredEventId, long highStoredEventId) {
        return storedEventsFrom(mapper.selectList(new LambdaQueryWrapper<StoredEventEntity>()
                .between(StoredEventEntity::getEventId,
                        lowStoredEventId,
                        highStoredEventId)
                .orderByAsc(StoredEventEntity::getEventId)));
    }

    @Override
    public List<StoredEvent> allStoredEventsSince(long storedEventId) {
        return storedEventsFrom(mapper.selectList(new LambdaQueryWrapper<StoredEventEntity>()
                .gt(StoredEventEntity::getEventId, storedEventId)
                .orderByAsc(StoredEventEntity::getEventId)));
    }

    private List<StoredEvent> storedEventsFrom(List<StoredEventEntity> entities) {
        return entities.stream()
                .map(this::storedEventFrom)
                .collect(Collectors.toList());
    }

    private StoredEvent storedEventFrom(StoredEventEntity entity) {
        return new StoredEvent(
                entity.getTypeName(),
                entity.getOccurredOn(),
                entity.getEventBody(),
                entity.getEventId()
        );
    }

    @Override
    public StoredEvent append(DomainEvent domainEvent) {
        String eventSerialization = EventSerializer.instance().serialize(domainEvent);

        StoredEvent storedEvent =
                new StoredEvent(
                        domainEvent.getClass().getName(),
                        domainEvent.getOccurredOn(),
                        eventSerialization,
                        identityFactory.create()
                );

        try {
            StoredEventEntity entity = storedEventEntityFrom(storedEvent);
            entity.setCreateTime(new Date());
            mapper.insert(entity);
        } catch (DuplicateKeyException e) {
            throw new IdempotentException();
        }

        return storedEvent;
    }

    private StoredEventEntity storedEventEntityFrom(StoredEvent event) {
        StoredEventEntity entity = new StoredEventEntity();
        entity.setEventId(event.eventId());
        entity.setTypeName(event.typeName());
        entity.setEventBody(event.eventBody());
        entity.setOccurredOn(event.occurredOn());
        entity.setCreateTime(event.occurredOn());
        entity.setUpdateTime(event.occurredOn());
        return entity;
    }

    @Override
    public void close() {
    }

    @Override
    public long countStoredEvents() {
        return mapper.selectCount(new LambdaQueryWrapper<>());
    }

    @Override
    public void removeStoredEventsBefore(long storedEventId) {
        mapper.delete(new LambdaQueryWrapper<StoredEventEntity>()
                .lt(StoredEventEntity::getEventId, storedEventId));
    }
}