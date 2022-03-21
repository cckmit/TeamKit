package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfig;

/**
 * 配置项开启事件
 *
 * @author jay.wu
 */
public class ConfigEnabledEvent extends ConfigValueChangedEvent {

    public ConfigEnabledEvent(SimpleConfig config) {
        super(config.getConfigId(), null, config.getConfigValue());
    }
}