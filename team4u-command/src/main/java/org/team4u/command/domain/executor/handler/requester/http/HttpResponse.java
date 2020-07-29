package org.team4u.command.domain.executor.handler.requester.http;

/**
 * 基于http的响应信息
 *
 * @author jay.wu
 */
public class HttpResponse {
    /**
     * http响应码
     */
    private int status;
    /**
     * http响应内容
     */
    private String body;

    public HttpResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return body;
    }
}