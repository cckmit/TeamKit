package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.infrastructure.util.JsonExtractor;

/**
 * 基于HTTP和JSON的命令处理器
 * <p>
 * 将JSON返回值转换为命令响应
 *
 * @author jay.wu
 */
public abstract class HttpJsonCommandRequester extends HttpCommandRequester {

    protected HttpJsonCommandRequester(HttpRequester httpRequester) {
        super(httpRequester);
    }

    @Override
    protected Object toCommandResponse(Context context, HttpRequester.HttpResponse response) {
        String body = response.getBody();

        JsonExtractor.ExtractConfig extractConfig = context.getConfig().itemOf(
                "jsonExtractor",
                JsonExtractor.ExtractConfig.class
        );
        return new JsonExtractor().extract(extractConfig, body);
    }
}