package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfig;

@Getter
public class ConfigCreatedEvent extends AbstractConfigChangedEvent<SimpleConfig> {

    private final SimpleConfig simpleConfig;

    public ConfigCreatedEvent(SimpleConfig simpleConfig) {
        super(simpleConfig.getConfigId(), null, simpleConfig, simpleConfig.getCreatedBy());
        this.simpleConfig = simpleConfig;
    }
}