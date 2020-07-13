package org.team4u.ddd.infrastructure.persistence.memory;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessages;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;

import java.util.Collections;
import java.util.List;

/**
 * 仅打印日志的事件资源库
 *
 * @author jay.wu
 */
public class LogOnlyEventStore implements EventStore {

    private final Log log = LogFactory.get();

    @Override
    public List<StoredEvent> allStoredEventsBetween(long lowStoredEventId, long highStoredEventId) {
        return Collections.emptyList();
    }

    @Override
    public List<StoredEvent> allStoredEventsSince(long storedEventId) {
        return Collections.emptyList();
    }

    @Override
    public StoredEvent append(DomainEvent domainEvent) {
        log.info(LogMessages.createWithMasker(this.getClass().getSimpleName(), "append")
                .append("event", domainEvent)
                .toString());

        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public long countStoredEvents() {
        return 0;
    }

    @Override
    public void removeStoredEventsBefore(long storedEventId) {
    }
}