package org.team4u.config.domain;

import cn.hutool.core.util.StrUtil;
import org.team4u.config.domain.event.*;
import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;
import java.util.Objects;

public class SimpleConfig extends AggregateRoot {

    private SimpleConfigId configId;

    private String configValue;

    private String description;

    private int sequenceNo;

    private Boolean enabled;

    private String createdBy;

    private String updatedBy;

    private Date createTime;

    private Date updateTime;

    public SimpleConfig(SimpleConfigId configId,
                        String configValue,
                        String description,
                        int sequenceNo,
                        Boolean enabled,
                        String createdBy,
                        Date createTime) {
        this.configId = configId;
        this.configValue = configValue;
        this.description = description;
        this.sequenceNo = sequenceNo;
        this.enabled = enabled;
        this.createdBy = createdBy;
        this.createTime = createTime;
    }

    public void create() {
        publishEvent(new ConfigCreatedEvent(
                getConfigId(),
                getConfigValue(),
                getDescription(),
                getSequenceNo(),
                getEnabled(),
                getCreatedBy(),
                getCreateTime()
        ));
    }

    public void enable(String updatedBy) {
        if (getEnabled()) {
            return;
        }

        setEnabled(true);
        setUpdatedBy(updatedBy);
        setUpdateTime(new Date());

        publishEvent(new ConfigEnabledEvent(getConfigId(), updatedBy));
    }

    public void disable(String updatedBy) {
        if (!getEnabled()) {
            return;
        }

        setEnabled(false);
        setUpdatedBy(updatedBy);
        setUpdateTime(new Date());

        publishEvent(new ConfigDisabledEvent(getConfigId(), updatedBy));
    }

    public void changeConfigValue(String newValue, String updatedBy) {
        if (StrUtil.equals(getConfigValue(), newValue)) {
            return;
        }

        String oldValue = getConfigValue();

        setConfigValue(newValue);
        setUpdatedBy(updatedBy);
        setUpdateTime(new Date());

        publishEvent(new ConfigValueChangedEvent(getConfigId(), oldValue, newValue, updatedBy));
    }

    public void changeSequenceNo(int newValue, String updatedBy) {
        if (getSequenceNo() == newValue) {
            return;
        }

        int oldValue = getSequenceNo();

        setSequenceNo(newValue);
        setUpdatedBy(updatedBy);
        setUpdateTime(new Date());

        publishEvent(new ConfigSequenceNoChangedEvent(getConfigId(), oldValue, newValue, updatedBy));
    }

    public void changeDescription(String newValue, String updatedBy) {
        if (StrUtil.equals(getDescription(), newValue)) {
            return;
        }

        String oldValue = getDescription();

        setDescription(newValue);
        setUpdatedBy(updatedBy);
        setUpdateTime(new Date());

        publishEvent(new ConfigDescChangedEvent(getConfigId(), oldValue, newValue, updatedBy));
    }

    public void delete() {
        publishEvent(new ConfigDeletedEvent(getConfigId(), updatedBy));
    }

    public SimpleConfigId getConfigId() {
        return configId;
    }

    private void setConfigId(SimpleConfigId configId) {
        this.configId = configId;
    }

    public String getConfigValue() {
        return configValue;
    }

    private void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    private void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    private void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    private void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    private void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    private void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleConfig that = (SimpleConfig) o;
        return sequenceNo == that.sequenceNo && configId.equals(that.configId) && Objects.equals(configValue, that.configValue) && Objects.equals(description, that.description) && enabled.equals(that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId, configValue, description, sequenceNo, enabled);
    }
}