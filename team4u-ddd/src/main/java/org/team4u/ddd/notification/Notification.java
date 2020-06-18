package org.team4u.ddd.notification;

import cn.hutool.core.lang.Assert;
import org.team4u.ddd.domain.model.DomainEvent;
import org.team4u.ddd.domain.model.ValueObject;

import java.util.Date;

public class Notification extends ValueObject {

    private DomainEvent event;
    private long notificationId;
    private Date occurredOn;
    private String typeName;
    private int version;

    public Notification(long notificationId, DomainEvent event) {
        this.setEvent(event);
        this.setNotificationId(notificationId);
        this.setOccurredOn(event.getOccurredOn());
        this.setTypeName(event.getClass().getName());
        this.setVersion(event.getVersion());
    }

    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> T getEvent() {
        return (T) this.event;
    }

    protected void setEvent(DomainEvent anEvent) {
        Assert.notNull(anEvent, "The event is required.");
        this.event = anEvent;
    }

    public long getNotificationId() {
        return notificationId;
    }

    protected void setNotificationId(long aNotificationId) {
        this.notificationId = aNotificationId;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    protected void setOccurredOn(Date anOccurredOn) {
        Assert.notNull(anOccurredOn, "The occurred date is required.");
        this.occurredOn = anOccurredOn;
    }

    public String getTypeName() {
        return typeName;
    }

    protected void setTypeName(String aTypeName) {
        Assert.notEmpty(aTypeName, "The type name is required.");
        this.typeName = aTypeName;
    }

    public int getVersion() {
        return version;
    }

    private void setVersion(int aVersion) {
        this.version = aVersion;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Notification typedObject = (Notification) anObject;
            equalObjects = this.getNotificationId() == typedObject.getNotificationId();
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return 3017 * 197
                + (int) this.getNotificationId();
    }

    @Override
    public String toString() {
        return "Notification [event=" + event + ", notificationId=" + notificationId
                + ", occurredOn=" + occurredOn + ", typeName="
                + typeName + ", version=" + version + "]";
    }
}