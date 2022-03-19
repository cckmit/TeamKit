package org.team4u.config.domain.event;

import cn.hutool.core.util.IdUtil;
import lombok.Getter;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.List;

@Getter
public class ConfigAllChangedEvent extends AbstractDomainEvent {

    private final List<AbstractConfigChangedEvent<?>> changedEvents;

    public ConfigAllChangedEvent(List<AbstractConfigChangedEvent<?>> changedEvents) {
        super(IdUtil.fastSimpleUUID());
        this.changedEvents = changedEvents;
    }
}