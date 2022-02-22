package org.team4u.command.domain.executor.handler.requester.http;

/**
 * 基于HTTP body的命令处理器
 * <p>
 * 将JSON返回值转换为命令响应
 *
 * @author jay.wu
 */
public abstract class HttpBodyCommandRequester extends HttpCommandRequester {

    protected HttpBodyCommandRequester(HttpRequester httpRequester) {
        super(httpRequester);
    }

    @Override
    protected Object toCommandResponse(Context context, HttpRequester.HttpResponse response) {
        return response.getBody();
    }
}