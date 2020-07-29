package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.domain.executor.handler.requester.CommandRequester;

/**
 * 基于HTTP的命令处理器
 *
 * @author jay.wu
 */
public abstract class HttpCommandHandler extends CommandRequester<HttpRequester.HttpRequest, HttpRequester.HttpResponse> {

    private final HttpRequester httpRequester;

    public HttpCommandHandler(HttpRequester httpRequester) {
        this.httpRequester = httpRequester;
    }

    @Override
    protected HttpRequester.HttpResponse execute(HttpRequester.HttpRequest httpRequest) {
        return httpRequester.execute(httpRequest);
    }
}