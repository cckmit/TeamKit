package org.team4u.command.handler.remote;

/**
 * 基于http的响应信息
 *
 * @author jay.wu
 */
public class SimpleHttpResponse {
    /**
     * http响应码
     */
    private final int status;
    /**
     * http响应内容
     */
    private final String body;

    public SimpleHttpResponse(int status, String body) {
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