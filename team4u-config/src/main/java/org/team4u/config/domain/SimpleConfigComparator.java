package org.team4u.config.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.message.jvm.MessagePublisher;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.config.domain.event.SimpleConfigEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 配置项比对器
 *
 * @author jay.wu
 */
public class SimpleConfigComparator {

    public List<SimpleConfigEvent> compare(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        List<SimpleConfigEvent> allEvents = new ArrayList<>();
        allEvents.addAll(compareUpdatedAndDeletedConfig(oldConfigs, newConfigs));
        allEvents.addAll(compareNewConfig(oldConfigs, newConfigs));
        return allEvents;
    }

    public void compareAndPublishEvents(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        publishEvents(compare(oldConfigs, newConfigs));
    }

    private List<SimpleConfigEvent> compareUpdatedAndDeletedConfig(List<SimpleConfig> oldConfigs,
                                                                   List<SimpleConfig> newConfigs) {
        return oldConfigs.stream()
                .map(oldConfig -> {
                    SimpleConfig newConfig = configOf(newConfigs, oldConfig.getConfigId());
                    compare(oldConfig, newConfig);
                    return eventsOf(oldConfig, newConfig);
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<SimpleConfigEvent> compareNewConfig(List<SimpleConfig> oldConfigs, List<SimpleConfig> newConfigs) {
        return newConfigs.stream()
                .map(newConfig -> {
                    SimpleConfig oldConfig = configOf(oldConfigs, newConfig.getConfigId());
                    // 新增
                    if (oldConfig != null) {
                        return null;
                    }

                    compare(null, newConfig);
                    return eventsOf(newConfig);
                })
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void publishEvents(List<SimpleConfigEvent> allEvents) {
        if(allEvents.isEmpty()){
            return;
        }

        MessagePublisher.instance().publish(new ConfigAllChangedEvent(allEvents));
        allEvents.forEach(it -> MessagePublisher.instance().publish(it));
    }

    public void compare(SimpleConfig oldConfig, SimpleConfig newConfig) {
        if (oldConfig == null) {
            if (newConfig != null) {
                newConfig.create();
            }

            return;
        }

        if (newConfig == null) {
            oldConfig.disable();
            return;
        }

        if (oldConfig.isEnabled() && !newConfig.isEnabled()) {
            oldConfig.disable();
            return;
        }

        if (!oldConfig.isEnabled() && newConfig.isEnabled()) {
            oldConfig.enable();
        }

        if (!StrUtil.equals(oldConfig.getConfigValue(), newConfig.getConfigValue())) {
            oldConfig.changeConfigValue(newConfig.getConfigValue());
        }

        if (!StrUtil.equals(oldConfig.getDescription(), newConfig.getDescription())) {
            oldConfig.changeDescription(newConfig.getDescription());
        }

        if (oldConfig.getSequenceNo() != newConfig.getSequenceNo()) {
            oldConfig.changeSequenceNo(newConfig.getSequenceNo());
        }
    }

    private List<? extends SimpleConfigEvent> eventsOf(SimpleConfig oldConfig, SimpleConfig newConfig) {
        List<? extends SimpleConfigEvent> allEvents = new ArrayList<>();
        CollUtil.addAll(allEvents, eventsOf(oldConfig));
        CollUtil.addAll(allEvents, eventsOf(newConfig));
        return allEvents;
    }

    private List<? extends SimpleConfigEvent> eventsOf(SimpleConfig config) {
        if (config == null) {
            return Collections.emptyList();
        }

        List<? extends SimpleConfigEvent> events = config.configEvents();
        config.clearEvents();
        return events;
    }

    private SimpleConfig configOf(List<SimpleConfig> configs, SimpleConfigId configId) {
        return configs.stream()
                .filter(it -> it.getConfigId().equals(configId))
                .findFirst()
                .orElse(null);
    }
}