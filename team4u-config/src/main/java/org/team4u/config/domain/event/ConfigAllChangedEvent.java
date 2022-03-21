package org.team4u.config.domain.event;

import cn.hutool.core.util.IdUtil;
import lombok.Getter;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 配置所有变更事件
 *
 * @author jay.wu
 */
@Getter
public class ConfigAllChangedEvent extends AbstractDomainEvent {

    private final List<? extends SimpleConfigEvent> allEvents;

    public ConfigAllChangedEvent(List<? extends SimpleConfigEvent> allEvents) {
        super(IdUtil.fastSimpleUUID());
        this.allEvents = allEvents;
    }

    /**
     * 获取所有变更的配置类型集合
     *
     * @author jay.wu
     */
    public Set<String> allConfigTypes() {
        return allEvents.stream()
                .map(it -> it.getConfigId().getConfigType())
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return allEvents.toString();
    }
}