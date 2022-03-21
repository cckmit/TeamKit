package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.config.domain.event.*;
import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置项
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SimpleConfig extends AggregateRoot {

    private final SimpleConfigId configId;

    private String configValue;

    private String description;

    private int sequenceNo;

    private Boolean enabled;

    public void create() {
        publishEvent(new ConfigCreatedEvent(this));
    }

    public void enable() {
        if (getEnabled()) {
            return;
        }

        setEnabled(true);

        publishEvent(new ConfigEnabledEvent(this));
    }

    public void disable() {
        if (!getEnabled()) {
            return;
        }

        setEnabled(false);

        publishEvent(new ConfigDisabledEvent(this));
    }

    public void changeConfigValue(String newValue) {
        if (StrUtil.equals(getConfigValue(), newValue)) {
            return;
        }

        String oldValue = getConfigValue();

        setConfigValue(newValue);

        publishEvent(new ConfigValueChangedEvent(getConfigId(), oldValue, newValue));
    }

    public void changeSequenceNo(int newValue) {
        if (getSequenceNo() == newValue) {
            return;
        }

        int oldValue = getSequenceNo();

        setSequenceNo(newValue);

        publishEvent(new ConfigSequenceNoChangedEvent(getConfigId(), oldValue, newValue));
    }

    public void changeDescription(String newValue) {
        if (StrUtil.equals(getDescription(), newValue)) {
            return;
        }

        String oldValue = getDescription();

        setDescription(newValue);

        publishEvent(new ConfigDescChangedEvent(getConfigId(), oldValue, newValue));
    }

    public List<? extends SimpleConfigEvent> configEvents() {
        return events().stream()
                .map(it -> (SimpleConfigEvent) it)
                .collect(Collectors.toList());
    }
}