package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

/**
 * 配置项禁用事件
 *
 * @author jay.wu
 */
public class ConfigDisabledEvent extends AbstractConfigChangedEvent<Boolean> {

    public ConfigDisabledEvent(SimpleConfigId simpleConfigId, String updatedBy) {
        super(simpleConfigId, true, false, updatedBy);
    }
}