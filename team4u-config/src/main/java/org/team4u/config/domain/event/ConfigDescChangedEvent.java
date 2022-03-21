package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

/**
 * 配置项描述变更事件
 *
 * @author jay.wu
 */
public class ConfigDescChangedEvent extends AbstractConfigChangedEvent<String> {

    public ConfigDescChangedEvent(SimpleConfigId simpleConfigId,
                                  String oldValue,
                                  String newValue) {
        super(simpleConfigId, oldValue, newValue);
    }
}