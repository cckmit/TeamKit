package org.team4u.config.domain.event;

import org.team4u.config.domain.SimpleConfigId;
import org.team4u.ddd.domain.model.AbstractDomainEvent;

import java.util.Date;

public class ConfigCreatedEvent extends AbstractDomainEvent {

    private final SimpleConfigId configId;

    private final String configValue;

    private final String description;

    private final int sequenceNo;

    private final Boolean enabled;

    private final String createdBy;

    private final Date createTime;

    public ConfigCreatedEvent(SimpleConfigId configId,
                              String configValue,
                              String description,
                              int sequenceNo,
                              Boolean enabled,
                              String createdBy,
                              Date createTime) {
        super(configId.toString(), createTime);
        this.configId = configId;
        this.configValue = configValue;
        this.description = description;
        this.sequenceNo = sequenceNo;
        this.enabled = enabled;
        this.createdBy = createdBy;
        this.createTime = createTime;
    }

    public SimpleConfigId getConfigId() {
        return configId;
    }

    public String getConfigValue() {
        return configValue;
    }

    public String getDescription() {
        return description;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
