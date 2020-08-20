package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

public abstract class AbstractConfigChangedEvent<V> extends AbstractDomainEvent {

    private final V oldValue;
    private final V newValue;
    private final String updatedBy;


    public AbstractConfigChangedEvent(SimpleConfigId simpleConfigId,
                                      V oldValue,
                                      V newValue,
                                      String updatedBy) {
        super(simpleConfigId.toString());
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updatedBy = updatedBy;
    }

    public V getOldValue() {
        return oldValue;
    }

    public V getNewValue() {
        return newValue;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
}
