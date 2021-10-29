package org.team4u.ddd.domain.model;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.message.MessagePublisher;
import org.team4u.ddd.message.MessageConsumer;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 领域事件发布者
 *
 * @author jay.wu
 */
public class DomainEventPublisher {

    private static final DomainEventPublisher INSTANCE = new DomainEventPublisher();

    private final Log log = LogFactory.get();
    private final MessagePublisher messagePublisher = new MessagePublisher();

    public static DomainEventPublisher instance() {
        return INSTANCE;
    }

    public void subscribe(List<MessageConsumer<? extends DomainEvent>> listeners) {
        messagePublisher.subscribe(listeners);
    }

    public void subscribe(MessageConsumer<? extends DomainEvent> listener) {
        messagePublisher.subscribe(listener);
    }

    public <T extends DomainEvent> void publish(T event) {
        if (log.isInfoEnabled()) {
            log.info(LogMessage.create(this.getClass().getSimpleName(), "publish")
                    .append("event", event)
                    .toString());
        }

        messagePublisher.publish(event);
    }

    public void publishAll(Collection<DomainEvent> domainEvents) {
        for (DomainEvent domainEvent : domainEvents) {
            this.publish(domainEvent);
        }
    }

    @SuppressWarnings("unchecked")
    public Set<MessageConsumer<? extends DomainEvent>> subscribers() {
        return messagePublisher.subscribers()
                .stream()
                .map(it -> (MessageConsumer<? extends DomainEvent>) it)
                .collect(Collectors.toSet());
    }
}