package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;

/**
 * 配置项序号变更事件
 *
 * @author jay.wu
 */
public class ConfigSequenceNoChangedEvent extends AbstractConfigChangedEvent<Integer> {

    public ConfigSequenceNoChangedEvent(SimpleConfigId simpleConfigId,
                                        Integer oldValue,
                                        Integer newValue,
                                        String updatedBy) {
        super(simpleConfigId, oldValue, newValue, updatedBy);
    }
}
