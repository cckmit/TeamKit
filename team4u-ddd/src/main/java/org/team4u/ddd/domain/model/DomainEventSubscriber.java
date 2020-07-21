package org.team4u.ddd.domain.model;

import org.team4u.ddd.message.MessageConsumer;

public interface DomainEventSubscriber<E extends DomainEvent> extends MessageConsumer<E> {

}