package org.team4u.command.domain.executor;

import java.util.Objects;

/**
 * 渠道响应值
 *
 * @author jay.wu
 */
public class ChannelResponse {

    /**
     * 一级响应码
     */
    private String code;
    /**
     * 一级响应消息
     */
    private String message;
    /**
     * 二级响应码
     */
    private String subCode;
    /**
     * 二级响应消息
     */
    private String subMessage;
    /**
     * 原始响应数据
     */
    private Object responseBody;

    public ChannelResponse() {
    }

    public ChannelResponse(String code, String message, String subCode, String subMessage, Object responseBody) {
        this.code = code;
        this.message = message;
        this.subCode = subCode;
        this.subMessage = subMessage;
        this.responseBody = responseBody;
    }

    public String getCode() {
        return code;
    }

    public ChannelResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ChannelResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getSubCode() {
        return subCode;
    }

    public ChannelResponse setSubCode(String subCode) {
        this.subCode = subCode;
        return this;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public ChannelResponse setSubMessage(String subMessage) {
        this.subMessage = subMessage;
        return this;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public ChannelResponse setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    @Override
    public String toString() {
        return "|channelResponseCode=" + code +
                "|channelResponseMessage=" + message +
                "|channelResponseSubCode=" + subCode +
                "|channelResponseSubMessage=" + subMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelResponse that = (ChannelResponse) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(message, that.message) &&
                Objects.equals(subCode, that.subCode) &&
                Objects.equals(subMessage, that.subMessage) &&
                Objects.equals(responseBody, that.responseBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, subCode, subMessage, responseBody);
    }
}
