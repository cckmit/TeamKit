package org.team4u.ddd.spring;

/**
 * @author Jay Wu
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.team4u.ddd.domain.model.DomainEventPublisher;
import org.team4u.ddd.domain.model.DomainEventSubscriber;

@Configuration
public class DddBeanConfig {
    @Bean
    public DomainEventPublisher domainEventPublisher(DomainEventSubscriber<?>[] subscribers) {
        DomainEventPublisher.instance().subscribe(subscribers);
        return DomainEventPublisher.instance();
    }
}
