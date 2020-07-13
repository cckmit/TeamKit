package org.team4u.ddd.domain.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;

import java.util.*;
import java.util.stream.Collectors;

public class DomainEventPublisher {

    private static final DomainEventPublisher instance = new DomainEventPublisher();
    private Log log = LogFactory.get();
    private List<DomainEventSubscriber<?>> subscribers = new ArrayList<>();

    public static DomainEventPublisher instance() {
        return instance;
    }

    public void subscribe(DomainEventSubscriber<?>... listeners) {
        this.subscribers.addAll(Arrays.asList(listeners));

        log.info(LogMessage.create(this.getClass().getSimpleName(), "subscribe")
                .success()
                .append("subscriber", CollUtil.join(
                        Arrays.stream(listeners)
                                .map(it -> it.getClass().getName())
                                .collect(Collectors.toList()),
                        ","))
                .toString());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends DomainEvent> void publish(T event) {
        for (DomainEventSubscriber subscriber : subscribers) {
            subscriber.onEvent(event);
        }
    }

    public void publishAll(Collection<DomainEvent> domainEvents) {
        for (DomainEvent domainEvent : domainEvents) {
            this.publish(domainEvent);
        }
    }

    public List<DomainEventSubscriber<?>> subscribers() {
        return Collections.unmodifiableList(subscribers);
    }
}