package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

/**
 * 配置项开启事件
 *
 * @author jay.wu
 */
public class ConfigEnabledEvent extends AbstractConfigChangedEvent<Boolean> {

    public ConfigEnabledEvent(SimpleConfigId simpleConfigId, String updatedBy) {
        super(simpleConfigId, false, true, updatedBy);
    }
}