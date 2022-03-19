package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

@Getter
public abstract class AbstractConfigChangedEvent<V> extends AbstractDomainEvent {

    private final SimpleConfigId configId;
    private final V oldValue;
    private final V newValue;
    private final String updatedBy;

    public AbstractConfigChangedEvent(SimpleConfigId simpleConfigId,
                                      V oldValue,
                                      V newValue,
                                      String updatedBy) {
        super(simpleConfigId.toString());
        this.configId = simpleConfigId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.updatedBy = updatedBy;
    }
}