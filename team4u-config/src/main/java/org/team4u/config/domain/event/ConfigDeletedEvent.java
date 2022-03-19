package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfig;

@Getter
public class ConfigDeletedEvent extends AbstractConfigChangedEvent<SimpleConfig> {

    private final SimpleConfig simpleConfig;

    public ConfigDeletedEvent(SimpleConfig simpleConfig) {
        super(simpleConfig.getConfigId(), simpleConfig, null, simpleConfig.getUpdatedBy());
        this.simpleConfig = simpleConfig;

    }
}