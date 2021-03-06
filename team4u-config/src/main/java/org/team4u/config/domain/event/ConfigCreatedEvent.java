package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfig;

/**
 * 配置项创建事件
 *
 * @author jay.wu
 */
@Getter
public class ConfigCreatedEvent extends ConfigValueChangedEvent {

    private final SimpleConfig simpleConfig;

    public ConfigCreatedEvent(SimpleConfig simpleConfig) {
        super(simpleConfig.getConfigId(), null, simpleConfig.getConfigValue());
        this.simpleConfig = simpleConfig;
    }
}