package org.team4u.command.handler.remote;

import cn.hutool.core.lang.Dict;

/**
 * 基于http的简单请求
 * <p>
 * 为http请求适配器提供足够信息
 *
 * @author jay.wu
 */
public class HttpConfig {
    /**
     * http方法
     */
    private String method;
    /**
     * http参数
     */
    private Dict params = Dict.create();
    /**
     * 请求地址
     */
    private String url;
    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeoutMillis = 1500;
    /**
     * 读超时时间（毫秒）
     */
    private int readTimeoutMillis = 2000;
    /**
     * 请求头信息
     */
    private Dict headers = Dict.create();
    /**
     * 请求体
     */
    private String body;

    public String getMethod() {
        return method;
    }

    public HttpConfig setMethod(String method) {
        this.method = method;
        return this;
    }

    public Dict getParams() {
        return params;
    }

    public HttpConfig setParams(Dict params) {
        this.params = params;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public Dict getHeaders() {
        return headers;
    }

    public HttpConfig setHeaders(Dict headers) {
        this.headers = headers;
        return this;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public HttpConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public HttpConfig setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
        return this;
    }

    public String getBody() {
        return body;
    }

    public HttpConfig setBody(String body) {
        this.body = body;
        return this;
    }
}
