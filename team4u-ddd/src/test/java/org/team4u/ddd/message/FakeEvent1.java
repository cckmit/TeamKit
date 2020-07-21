package org.team4u.ddd.message;

import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.Date;

public class FakeEvent1 extends AbstractDomainEvent {

    public FakeEvent1(String domainId) {
        super(domainId);
    }

    public FakeEvent1(String domainId, Date occurredOn) {
        super(domainId, occurredOn);
    }

    public FakeEvent1(String domainId, Date occurredOn, int version) {
        super(domainId, occurredOn, version);
    }
}
