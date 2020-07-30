package org.team4u.command.domain.executor;

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
}
