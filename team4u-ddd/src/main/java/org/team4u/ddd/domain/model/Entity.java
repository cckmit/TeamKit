package org.team4u.ddd.domain.model;


import org.team4u.base.log.LogMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类
 *
 * @author jay.wu
 */
public abstract class Entity extends IdentifiedDomainObject {

    private transient final List<DomainEvent> events = new ArrayList<>();

    public List<DomainEvent> events() {
        return events;
    }

    public void clearEvents() {
        events.clear();
    }

    protected void publishEvent(DomainEvent event) {
        log.info(LogMessages.createWithMasker(this.getClass().getSimpleName(), "publishEvent")
                .append("event", event)
                .toString());
        events().add(event);
    }
}