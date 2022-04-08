package org.team4u.command.domain.executor.handler.log;

import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;

/**
 * 命令日志
 *
 * @author jay.wu
 */
public class CommandLog extends AggregateRoot {

    /**
     * 命令请求
     */
    private Object request;
    /**
     * 命令响应
     */
    private Object response;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 渠道处理耗时（毫秒）
     */
    private Long durationMillisecond;
    /**
     * 是否刚刚保存成功
     */
    private transient boolean saveBefore;

    public CommandLog(Object request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    public <Request> Request getRequest() {
        return (Request) request;
    }

    public CommandLog setRequest(Object request) {
        this.request = request;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <Response> Response getResponse() {
        return (Response) response;
    }

    public CommandLog setResponse(Object response) {
        this.response = response;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public CommandLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public CommandLog setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getDurationMillisecond() {
        return durationMillisecond;
    }

    public CommandLog setDurationMillisecond(Long durationMillisecond) {
        this.durationMillisecond = durationMillisecond;
        return this;
    }

    public boolean isSaveBefore() {
        return saveBefore;
    }

    public CommandLog setSaveBefore(boolean saveBefore) {
        this.saveBefore = saveBefore;
        return this;
    }
}
