package org.team4u.command.domain.executor.handler.requester.http;

import cn.hutool.core.lang.Dict;

/**
 * 基于HTTP的请求者
 *
 * @author jay.wu
 */
public interface HttpRequester {

    HttpResponse execute(HttpRequest request);

    /**
     * 基于http的简单请求
     * <p>
     * 为http请求适配器提供足够信
     */
    class HttpRequest {
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
        private int readTimeoutMillis = 5000;
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

        public HttpRequest setMethod(String method) {
            this.method = method;
            return this;
        }

        public Dict getParams() {
            return params;
        }

        public HttpRequest setParams(Dict params) {
            this.params = params;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public HttpRequest setUrl(String url) {
            this.url = url;
            return this;
        }

        public Dict getHeaders() {
            return headers;
        }

        public HttpRequest setHeaders(Dict headers) {
            this.headers = headers;
            return this;
        }

        public int getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public HttpRequest setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            return this;
        }

        public int getReadTimeoutMillis() {
            return readTimeoutMillis;
        }

        public HttpRequest setReadTimeoutMillis(int readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        public String getBody() {
            return body;
        }

        public HttpRequest setBody(String body) {
            this.body = body;
            return this;
        }
    }

    /**
     * 基于http的响应信息
     */
    class HttpResponse {
        /**
         * http响应码
         */
        private final int status;
        /**
         * http响应内容
         */
        private final String body;

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
}
