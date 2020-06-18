package org.team4u.ddd.domain.model;

import java.util.Date;

public class FakeEvent2 extends AbstractDomainEvent {

    public FakeEvent2(String domainId) {
        super(domainId);
    }

    public FakeEvent2(String domainId, Date occurredOn) {
        super(domainId, occurredOn);
    }

    public FakeEvent2(String domainId, Date occurredOn, int version) {
        super(domainId, occurredOn, version);
    }
}
