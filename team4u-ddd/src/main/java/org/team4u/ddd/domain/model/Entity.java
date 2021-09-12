package org.team4u.ddd.domain.model;


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
        events().add(event);
    }
}