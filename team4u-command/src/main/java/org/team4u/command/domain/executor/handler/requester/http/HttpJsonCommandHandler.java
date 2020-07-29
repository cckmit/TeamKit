package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandResponse;
import org.team4u.command.infrastructure.util.JsonExtractor;

/**
 * 基于HTTP和JSON的命令处理器
 *
 * @author jay.wu
 */
public abstract class HttpJsonCommandHandler extends HttpCommandHandler {

    protected HttpJsonCommandHandler(HttpRequester httpRequester) {
        super(httpRequester);
    }

    /**
     * 根据配置，将JSON返回值转换为命令响应
     *
     * @param config   命令配置
     * @param response 请求者特定的响应对象
     * @return 命令响应
     */
    @Override
    protected CommandResponse toCommandResponse(CommandConfig config, HttpRequester.HttpResponse response) {
        String body = response.getBody();
        JsonExtractor.ExtractConfig extractConfig = config.itemOf(
                "jsonExtractor",
                JsonExtractor.ExtractConfig.class
        );
        return new JsonExtractor().extract(extractConfig, body);
    }
}