package org.team4u.ddd.event;

import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.domain.model.DomainEventSubscriber;

/**
 * 领域事件监听器，用于存储事件
 */
public class StoreDomainEventSubscriber implements DomainEventSubscriber<DomainEvent> {

    private EventStore eventStore;

    public StoreDomainEventSubscriber(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void onMessage(DomainEvent event) {
        eventStore.append(event);
    }
}