package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfig;

/**
 * 配置项删除变更事件
 *
 * @author jay.wu
 */
@Getter
public class ConfigDeletedEvent extends AbstractConfigChangedEvent<SimpleConfig> {

    private final SimpleConfig simpleConfig;

    public ConfigDeletedEvent(SimpleConfig simpleConfig) {
        super(simpleConfig.getConfigId(), simpleConfig, null, simpleConfig.getUpdatedBy());
        this.simpleConfig = simpleConfig;

    }
}