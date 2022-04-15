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
    /**
     * 配置标识
     */
    private final SimpleConfigId configId;
    /**
     * 配置值
     */
    private String configValue;
    /**
     * 配置描述
     */
    private String description;
    /**
     * 配置顺序
     */
    private int sequenceNo;
    /**
     * 是否启用配置
     */
    private boolean enabled;

    public void create() {
        publishEvent(new ConfigCreatedEvent(this));
    }

    public void enable() {
        if (isEnabled()) {
            return;
        }

        setEnabled(true);

        publishEvent(new ConfigEnabledEvent(this));
    }

    public void disable() {
        if (!isEnabled()) {
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