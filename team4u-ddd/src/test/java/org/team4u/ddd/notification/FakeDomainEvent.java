package org.team4u.ddd.notification;


import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.ddd.TestUtil;

public class FakeDomainEvent extends AbstractDomainEvent {

    private String name;

    public FakeDomainEvent(String aName) {
        super(TestUtil.TEST_ID);
        this.setName(aName);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }
}
