package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

public class ConfigSequenceNoChangedEvent extends AbstractConfigChangedEvent<Integer> {

    public ConfigSequenceNoChangedEvent(SimpleConfigId simpleConfigId,
                                        Integer oldValue,
                                        Integer newValue,
                                        String updatedBy) {
        super(simpleConfigId, oldValue, newValue, updatedBy);
    }
}
