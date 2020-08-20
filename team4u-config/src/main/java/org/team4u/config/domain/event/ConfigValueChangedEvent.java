package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

public class ConfigValueChangedEvent extends AbstractConfigChangedEvent<String> {

    public ConfigValueChangedEvent(SimpleConfigId simpleConfigId,
                                   String oldValue,
                                   String newValue,
                                   String updatedBy) {
        super(simpleConfigId, oldValue, newValue, updatedBy);
    }
}
