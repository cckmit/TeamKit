package org.team4u.ddd.event;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.domain.model.IdentifiedValueObject;

import java.util.Date;

public class StoredEvent extends IdentifiedValueObject {

    private String eventBody;
    private long eventId;
    private Date occurredOn;
    private String typeName;

    public StoredEvent(String typeName, Date occurredOn, String eventBody, long eventId) {
        this.setTypeName(typeName);
        this.setOccurredOn(occurredOn);
        this.setEventBody(eventBody);
        this.setEventId(eventId);
    }

    public String eventBody() {
        return this.eventBody;
    }

    public long eventId() {
        return this.eventId;
    }

    public Date occurredOn() {
        return this.occurredOn;
    }

    public <T extends DomainEvent> T toDomainEvent() {
        Class<T> domainEventClass = ClassUtil.loadClass(typeName());
        return EventSerializer.instance().deserialize(this.eventBody(), domainEventClass);
    }

    public String typeName() {
        return this.typeName;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            StoredEvent typedObject = (StoredEvent) anObject;
            equalObjects = this.eventId() == typedObject.eventId();
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return 1237 * 233 + (int) this.eventId();
    }

    @Override
    public String toString() {
        return "StoredEvent [eventBody=" + eventBody + ", eventId=" + eventId + ", occurredOn=" + occurredOn + ", typeName="
                + typeName + "]";
    }

    protected void setEventBody(String anEventBody) {
        Assert.notEmpty(anEventBody, "The event body is required.");
        this.eventBody = anEventBody;
    }

    protected void setEventId(long anEventId) {
        this.eventId = anEventId;
    }

    protected void setOccurredOn(Date anOccurredOn) {
        this.occurredOn = anOccurredOn;
    }

    protected void setTypeName(String aTypeName) {
        Assert.notEmpty(aTypeName, "The event type name is required.");
        this.typeName = aTypeName;
    }
}
