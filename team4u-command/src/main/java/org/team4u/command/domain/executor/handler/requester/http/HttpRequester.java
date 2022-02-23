package org.team4u.command.domain.executor.handler.requester.http;

import cn.hutool.core.lang.Dict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class HttpRequest {
        /**
         * http方法
         */
        private String method;
        /**
         * http参数
         */
        @Builder.Default
        private Dict params = Dict.create();
        /**
         * 请求地址
         */
        private String url;
        /**
         * 连接超时时间（毫秒）
         */
        @Builder.Default
        private int connectTimeoutMillis = 1500;
        /**
         * 读超时时间（毫秒）
         */
        @Builder.Default
        private int readTimeoutMillis = 5000;
        /**
         * 请求头信息
         */
        @Builder.Default
        private Dict headers = Dict.create();
        /**
         * 请求体
         */
        private String body;
        /**
         * 扩展信息
         */
        @Builder.Default
        private Dict ext = Dict.create();
    }

    /**
     * 基于http的响应信息
     */
    @Data
    class HttpResponse {
        /**
         * http响应码
         */
        private final int status;
        /**
         * http响应内容
         */
        private final String body;
    }
}
