package org.team4u.ddd.infrastructure.spring;

/**
 * @author jay.wu
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.team4u.base.message.jvm.MessageSubscriber;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.domain.model.DomainEventPublisher;

import java.util.List;

@Configuration
public class DddBeanConfig {
    @Bean
    public DomainEventPublisher domainEventPublisher(List<MessageSubscriber<? extends DomainEvent>> subscribers) {
        DomainEventPublisher.instance().subscribe(subscribers);
        return DomainEventPublisher.instance();
    }
}
