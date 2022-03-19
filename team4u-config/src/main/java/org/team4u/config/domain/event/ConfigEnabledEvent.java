package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

public class ConfigEnabledEvent extends AbstractConfigChangedEvent<Boolean> {

    public ConfigEnabledEvent(SimpleConfigId simpleConfigId, String updatedBy) {
        super(simpleConfigId, false, true, updatedBy);
    }
}
