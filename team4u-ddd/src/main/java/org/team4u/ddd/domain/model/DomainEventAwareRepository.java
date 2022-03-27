package org.team4u.ddd.domain.model;


import org.team4u.ddd.event.EventStore;

/**
 * 可处理领域事件的资源库
 *
 * @author jay.wu
 */
public abstract class DomainEventAwareRepository<E extends Entity> implements DomainRepository<E> {

    private final EventStore eventStore;

    public DomainEventAwareRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void save(E aggregateRoot) {
        appendToEventStore(aggregateRoot);

        doSave(aggregateRoot);

        publishEvents(aggregateRoot);
    }

    private void publishEvents(E aggregateRoot) {
        DomainEventPublisher.instance().publishAll(aggregateRoot.events());
        aggregateRoot.clearEvents();
    }

    private void appendToEventStore(E aggregateRoot) {
        if (aggregateRoot.events().isEmpty()) {
            return;
        }

        for (DomainEvent event : aggregateRoot.events()) {
            if (event instanceof TransientDomainEvent) {
                continue;
            }
            eventStore.append(event);
        }
    }

    protected abstract void doSave(E aggregateRoot);
}