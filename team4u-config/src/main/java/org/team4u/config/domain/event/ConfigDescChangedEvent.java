package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

public class ConfigDescChangedEvent extends AbstractConfigChangedEvent<String> {

    public ConfigDescChangedEvent(SimpleConfigId simpleConfigId,
                                  String oldValue,
                                  String newValue,
                                  String updatedBy) {
        super(simpleConfigId, oldValue, newValue, updatedBy);
    }
}
