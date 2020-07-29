package org.team4u.command.domain.executor;

import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;

public class CommandLog extends AggregateRoot {

    /**
     * 标准请求值
     */
    private CommandRequest request;

    private CommandResponse response;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public CommandLog(CommandRequest request) {
        this.request = request;
    }

    public CommandRequest getRequest() {
        return request;
    }

    public CommandLog setRequest(CommandRequest request) {
        this.request = request;
        return this;
    }

    public CommandResponse getResponse() {
        return response;
    }

    public CommandLog setResponse(CommandResponse response) {
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
