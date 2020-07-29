package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.executor.CommandResponse;

public class MockCommandResponse implements CommandResponse {

    /**
     * 标准响应码
     */
    private String standardCode;
    /**
     * 一级响应码
     */
    private String channelCode;
    /**
     * 一级响应消息
     */
    private String channelMessage;
    /**
     * 二级响应码
     */
    private String subChannelCode;
    /**
     * 二级响应消息
     */
    private String subChannelMessage;
    /**
     * 原始响应数据
     */
    private Object channelRawBody;

    public String getStandardCode() {
        return standardCode;
    }

    public MockCommandResponse setStandardCode(String standardCode) {
        this.standardCode = standardCode;
        return this;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public MockCommandResponse setChannelCode(String channelCode) {
        this.channelCode = channelCode;
        return this;
    }

    public String getChannelMessage() {
        return channelMessage;
    }

    public MockCommandResponse setChannelMessage(String channelMessage) {
        this.channelMessage = channelMessage;
        return this;
    }

    public String getSubChannelCode() {
        return subChannelCode;
    }

    public MockCommandResponse setSubChannelCode(String subChannelCode) {
        this.subChannelCode = subChannelCode;
        return this;
    }

    public String getSubChannelMessage() {
        return subChannelMessage;
    }

    public MockCommandResponse setSubChannelMessage(String subChannelMessage) {
        this.subChannelMessage = subChannelMessage;
        return this;
    }

    public Object getChannelRawBody() {
        return channelRawBody;
    }

    public MockCommandResponse setChannelRawBody(Object channelRawBody) {
        this.channelRawBody = channelRawBody;
        return this;
    }
}
