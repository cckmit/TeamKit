package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

public class ConfigDisabledEvent extends AbstractDomainEvent {

    private final String updatedBy;

    public ConfigDisabledEvent(SimpleConfigId simpleConfigId,
                               String updatedBy) {
        super(simpleConfigId.toString());
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
}
