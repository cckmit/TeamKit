package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.util.StrUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.ddd.domain.model.DomainEventPublisher;

import java.util.List;

public class CacheableSimpleConfigRepository implements SimpleConfigRepository {

    private final Config config;
    private final SimpleConfigRepository delegateConfigRepository;

    private List<SimpleConfig> allConfigs;
    private long lastUpdateTimeMillis;

    public CacheableSimpleConfigRepository(Config config, SimpleConfigRepository delegateConfigRepository) {
        this.config = config;
        this.delegateConfigRepository = delegateConfigRepository;

        refresh();
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        if (isExpired()) {
            List<SimpleConfig> oldConfigs = allConfigs;
            refresh();
            diff(oldConfigs, allConfigs);
        }

        return allConfigs;
    }

    private void refresh() {
        allConfigs = delegateConfigRepository.allConfigs();
        lastUpdateTimeMillis = System.currentTimeMillis();
    }

    private void diff(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        for (SimpleConfig oldConfig : oldConfigs) {
            SimpleConfig newConfig = configOf(newConfigs, oldConfig.getConfigId());

            // 更新、删除
            diff(oldConfig, newConfig);
        }

        for (SimpleConfig newConfig : newConfigs) {
            SimpleConfig oldConfig = configOf(oldConfigs, newConfig.getConfigId());

            // 新增
            if (oldConfig == null) {
                diff(null, newConfig);
            }
        }
    }

    private SimpleConfig configOf(List<SimpleConfig> configs, SimpleConfigId configId) {
        return configs.stream()
                .filter(it -> it.getConfigId().equals(configId))
                .findFirst()
                .orElse(null);
    }

    private void diff(SimpleConfig oldConfig, SimpleConfig newConfig) {
        if (oldConfig == null) {
            if (newConfig != null) {
                newConfig.create();
                DomainEventPublisher.instance().publishAll(newConfig.events());
                newConfig.clearEvents();
            }

            return;
        }

        if (newConfig == null) {
            oldConfig.delete();
            DomainEventPublisher.instance().publishAll(oldConfig.events());
            oldConfig.clearEvents();
            return;
        }

        if (!oldConfig.getEnabled() && newConfig.getEnabled()) {
            oldConfig.enable(newConfig.getUpdatedBy());
            DomainEventPublisher.instance().publishAll(oldConfig.events());
            oldConfig.clearEvents();
            return;
        }

        if (oldConfig.getEnabled() && !newConfig.getEnabled()) {
            oldConfig.disable(newConfig.getUpdatedBy());
            DomainEventPublisher.instance().publishAll(oldConfig.events());
            return;
        }

        if (!StrUtil.equals(oldConfig.getConfigValue(), newConfig.getConfigValue())) {
            oldConfig.changeConfigValue(newConfig.getConfigValue(), newConfig.getUpdatedBy());
            DomainEventPublisher.instance().publishAll(oldConfig.events());
        }

        if (!StrUtil.equals(oldConfig.getDescription(), newConfig.getDescription())) {
            oldConfig.changeDescription(newConfig.getDescription(), newConfig.getUpdatedBy());
            DomainEventPublisher.instance().publishAll(oldConfig.events());
        }

        if (oldConfig.getSequenceNo() != newConfig.getSequenceNo()) {
            oldConfig.changeSequenceNo(newConfig.getSequenceNo(), newConfig.getUpdatedBy());
            DomainEventPublisher.instance().publishAll(oldConfig.events());
        }

        oldConfig.clearEvents();
        newConfig.clearEvents();
    }

    private boolean isExpired() {
        return System.currentTimeMillis() - lastUpdateTimeMillis > config.getMaxEffectiveSec() * 1000;
    }

    public static class Config {
        private int maxEffectiveSec = 60;

        public int getMaxEffectiveSec() {
            return maxEffectiveSec;
        }

        public Config setMaxEffectiveSec(int maxEffectiveSec) {
            this.maxEffectiveSec = maxEffectiveSec;
            return this;
        }
    }
}