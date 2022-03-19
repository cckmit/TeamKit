package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.DomainEventPublisher;

import java.util.List;

/**
 * 配置项比对器
 *
 * @author jay.wu
 */
public class SimpleConfigComparator {

    public void compare(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        for (SimpleConfig oldConfig : oldConfigs) {
            SimpleConfig newConfig = configOf(newConfigs, oldConfig.getConfigId());

            // 更新、删除
            compare(oldConfig, newConfig);
        }

        for (SimpleConfig newConfig : newConfigs) {
            SimpleConfig oldConfig = configOf(oldConfigs, newConfig.getConfigId());

            // 新增
            if (oldConfig == null) {
                compare(null, newConfig);
            }
        }
    }

    public void compare(SimpleConfig oldConfig, SimpleConfig newConfig) {
        if (oldConfig == null) {
            if (newConfig != null) {
                newConfig.create();
                publishEvent(newConfig);
            }

            return;
        }

        if (newConfig == null) {
            oldConfig.delete();
            publishEvent(oldConfig);
            return;
        }

        if (!oldConfig.getEnabled() && newConfig.getEnabled()) {
            oldConfig.enable(newConfig.getUpdatedBy());
            publishEvent(oldConfig);
            return;
        }

        if (oldConfig.getEnabled() && !newConfig.getEnabled()) {
            oldConfig.disable(newConfig.getUpdatedBy());
            publishEvent(oldConfig);
            return;
        }

        if (!StrUtil.equals(oldConfig.getConfigValue(), newConfig.getConfigValue())) {
            oldConfig.changeConfigValue(newConfig.getConfigValue(), newConfig.getUpdatedBy());
            publishEvent(oldConfig);
        }

        if (!StrUtil.equals(oldConfig.getDescription(), newConfig.getDescription())) {
            oldConfig.changeDescription(newConfig.getDescription(), newConfig.getUpdatedBy());
            publishEvent(oldConfig);
        }

        if (oldConfig.getSequenceNo() != newConfig.getSequenceNo()) {
            oldConfig.changeSequenceNo(newConfig.getSequenceNo(), newConfig.getUpdatedBy());
            publishEvent(oldConfig);
        }
    }

    private void publishEvent(SimpleConfig config) {
        DomainEventPublisher.instance().publishAll(config.events());
        config.clearEvents();
    }

    private SimpleConfig configOf(List<SimpleConfig> configs, SimpleConfigId configId) {
        return configs.stream()
                .filter(it -> it.getConfigId().equals(configId))
                .findFirst()
                .orElse(null);
    }
}