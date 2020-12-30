package org.team4u.ddd.infrastructure.persistence.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.util.Date;

@TableName("time_constrained_process_tracker")
public class TimeConstrainedProcessTrackerEntity {
    /**
     * 自增长标识
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 跟踪器标识
     */
    private String trackerId;

    /**
     * 关联处理标识
     */
    private String processId;

    /**
     * 是否完成(0:未完成，1:已完成)
     */
    private Boolean completed;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否完成所有超时通知
     */
    private Boolean processInformedOfTimeout;

    /**
     * 超时时间类型
     */
    private String processTimedOutEventType;

    /**
     * 超时时间
     */
    private Date timeoutOccursOn;
    /**
     * 重试策略
     */
    private String retryStrategyId;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 乐观锁版本
     */
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public TimeConstrainedProcessTrackerEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTrackerId() {
        return trackerId;
    }

    public TimeConstrainedProcessTrackerEntity setTrackerId(String trackerId) {
        this.trackerId = trackerId;
        return this;
    }

    public String getProcessId() {
        return processId;
    }

    public TimeConstrainedProcessTrackerEntity setProcessId(String processId) {
        this.processId = processId;
        return this;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public TimeConstrainedProcessTrackerEntity setCompleted(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TimeConstrainedProcessTrackerEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Boolean getProcessInformedOfTimeout() {
        return processInformedOfTimeout;
    }

    public TimeConstrainedProcessTrackerEntity setProcessInformedOfTimeout(Boolean processInformedOfTimeout) {
        this.processInformedOfTimeout = processInformedOfTimeout;
        return this;
    }

    public String getProcessTimedOutEventType() {
        return processTimedOutEventType;
    }

    public TimeConstrainedProcessTrackerEntity setProcessTimedOutEventType(String processTimedOutEventType) {
        this.processTimedOutEventType = processTimedOutEventType;
        return this;
    }

    public Date getTimeoutOccursOn() {
        return timeoutOccursOn;
    }

    public TimeConstrainedProcessTrackerEntity setTimeoutOccursOn(Date timeoutOccursOn) {
        this.timeoutOccursOn = timeoutOccursOn;
        return this;
    }

    public String getRetryStrategyId() {
        return retryStrategyId;
    }

    public TimeConstrainedProcessTrackerEntity setRetryStrategyId(String retryStrategyId) {
        this.retryStrategyId = retryStrategyId;
        return this;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public TimeConstrainedProcessTrackerEntity setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public TimeConstrainedProcessTrackerEntity setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public TimeConstrainedProcessTrackerEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public TimeConstrainedProcessTrackerEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}