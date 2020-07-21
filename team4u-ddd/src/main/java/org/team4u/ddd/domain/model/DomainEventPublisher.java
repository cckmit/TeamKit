package org.team4u.ddd.domain.model;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.ddd.message.MessageConsumer;

import java.util.*;

public class DomainEventPublisher {

    private static final DomainEventPublisher INSTANCE = new DomainEventPublisher();
    private final Log log = LogFactory.get();
    private final Set<MessageConsumer<? extends DomainEvent>> subscribers = new HashSet<>();

    public static DomainEventPublisher instance() {
        return INSTANCE;
    }

    public void subscribe(List<MessageConsumer<? extends DomainEvent>> listeners) {
        for (MessageConsumer<? extends DomainEvent> listener : listeners) {
            subscribe(listener);
        }
    }

    public void subscribe(MessageConsumer<? extends DomainEvent> listener) {
        subscribers.add(listener);

        log.info(LogMessage.create(this.getClass().getSimpleName(), "subscribe")
                .success()
                .append("subscriber", listener.getClass().getSimpleName())
                .toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends DomainEvent> void publish(T event) {
        for (MessageConsumer subscriber : subscribers) {
            subscriber.processMessage(event);
        }
    }

    public void publishAll(Collection<DomainEvent> domainEvents) {
        for (DomainEvent domainEvent : domainEvents) {
            this.publish(domainEvent);
        }
    }

    public Set<MessageConsumer<? extends DomainEvent>> subscribers() {
        return Collections.unmodifiableSet(subscribers);
    }
}