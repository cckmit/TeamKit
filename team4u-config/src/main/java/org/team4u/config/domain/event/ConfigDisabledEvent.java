package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfig;

/**
 * 配置项禁用事件
 *
 * @author jay.wu
 */
public class ConfigDisabledEvent extends ConfigValueChangedEvent {

    public ConfigDisabledEvent(SimpleConfig config) {
        super(config.getConfigId(), config.getConfigValue(), null);
    }
}