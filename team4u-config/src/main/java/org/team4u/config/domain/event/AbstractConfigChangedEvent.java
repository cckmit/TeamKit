package org.team4u.config.domain.event;

import lombok.Getter;
import org.team4u.config.domain.SimpleConfigId;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

/**
 * 抽象配置项变更事件
 *
 * @author jay.wu
 */
@Getter
public abstract class AbstractConfigChangedEvent<V> extends AbstractDomainEvent implements SimpleConfigEvent {

    private final SimpleConfigId configId;
    private final V oldValue;
    private final V newValue;

    public AbstractConfigChangedEvent(SimpleConfigId simpleConfigId,
                                      V oldValue,
                                      V newValue) {
        super(simpleConfigId.toString());
        this.configId = simpleConfigId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "|" + configId.toString();
    }
}