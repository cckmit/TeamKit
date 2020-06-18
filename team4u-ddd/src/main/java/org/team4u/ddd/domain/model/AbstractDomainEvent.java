package org.team4u.ddd.domain.model;

import java.util.Date;

public class AbstractDomainEvent implements DomainEvent {

    private String domainId;

    private Date occurredOn;

    private int version;

    public AbstractDomainEvent(String domainId) {
        this(domainId, new Date());
    }

    public AbstractDomainEvent(String domainId, Date occurredOn) {
        this(domainId, occurredOn, 1);
    }

    public AbstractDomainEvent(String domainId, Date occurredOn, int version) {
        this.domainId = domainId;
        this.occurredOn = occurredOn;
        this.version = version;
    }


    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public Date getOccurredOn() {
        return occurredOn;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "domainId='" + domainId + '\'' +
                ", occurredOn=" + occurredOn +
                ", version=" + version +
                '}';
    }
}