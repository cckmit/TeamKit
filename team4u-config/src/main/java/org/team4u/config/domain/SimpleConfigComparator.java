package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import org.team4u.config.domain.event.AbstractConfigChangedEvent;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.ddd.domain.model.DomainEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配置项比对器
 *
 * @author jay.wu
 */
public class SimpleConfigComparator {

    public void compare(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        List<AbstractConfigChangedEvent<?>> changedEvents = new ArrayList<>();
        for (SimpleConfig oldConfig : oldConfigs) {
            SimpleConfig newConfig = configOf(newConfigs, oldConfig.getConfigId());

            // 更新、删除
            compare(changedEvents, oldConfig, newConfig);
        }

        for (SimpleConfig newConfig : newConfigs) {
            SimpleConfig oldConfig = configOf(oldConfigs, newConfig.getConfigId());

            // 新增
            if (oldConfig == null) {
                compare(changedEvents, null, newConfig);
            }
        }

        if (!changedEvents.isEmpty()) {
            DomainEventPublisher.instance().publish(new ConfigAllChangedEvent(changedEvents));
        }
    }

    public void compare(List<AbstractConfigChangedEvent<?>> changedEvents,
                        SimpleConfig oldConfig,
                        SimpleConfig newConfig) {
        if (oldConfig == null) {
            if (newConfig != null) {
                newConfig.create();
                publishEvent(changedEvents, newConfig);
            }

            return;
        }

        if (newConfig == null) {
            oldConfig.delete();
            publishEvent(changedEvents, oldConfig);
            return;
        }

        if (!oldConfig.getEnabled() && newConfig.getEnabled()) {
            oldConfig.enable(newConfig.getUpdatedBy());
            publishEvent(changedEvents, oldConfig);
            return;
        }

        if (oldConfig.getEnabled() && !newConfig.getEnabled()) {
            oldConfig.disable(newConfig.getUpdatedBy());
            publishEvent(changedEvents, oldConfig);
            return;
        }

        if (!StrUtil.equals(oldConfig.getConfigValue(), newConfig.getConfigValue())) {
            oldConfig.changeConfigValue(newConfig.getConfigValue(), newConfig.getUpdatedBy());
            publishEvent(changedEvents, oldConfig);
        }

        if (!StrUtil.equals(oldConfig.getDescription(), newConfig.getDescription())) {
            oldConfig.changeDescription(newConfig.getDescription(), newConfig.getUpdatedBy());
            publishEvent(changedEvents, oldConfig);
        }

        if (oldConfig.getSequenceNo() != newConfig.getSequenceNo()) {
            oldConfig.changeSequenceNo(newConfig.getSequenceNo(), newConfig.getUpdatedBy());
            publishEvent(changedEvents, oldConfig);
        }
    }

    private void publishEvent(List<AbstractConfigChangedEvent<?>> changedEvents, SimpleConfig config) {
        DomainEventPublisher.instance().publishAll(config.events());
        changedEvents.addAll(config.events().stream()
                .map(it -> (AbstractConfigChangedEvent<?>) it)
                .collect(Collectors.toList()));
        config.clearEvents();
    }

    private SimpleConfig configOf(List<SimpleConfig> configs, SimpleConfigId configId) {
        return configs.stream()
                .filter(it -> it.getConfigId().equals(configId))
                .findFirst()
                .orElse(null);
    }
}