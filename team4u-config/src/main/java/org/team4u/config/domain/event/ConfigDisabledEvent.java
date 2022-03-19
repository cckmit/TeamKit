package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

public class ConfigDisabledEvent extends AbstractConfigChangedEvent<Boolean> {

    public ConfigDisabledEvent(SimpleConfigId simpleConfigId, String updatedBy) {
        super(simpleConfigId, true, false, updatedBy);
    }
}