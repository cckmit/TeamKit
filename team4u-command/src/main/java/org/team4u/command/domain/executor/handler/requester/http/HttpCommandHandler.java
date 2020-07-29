package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.domain.executor.handler.requester.CommandRequester;

public abstract class HttpCommandHandler extends CommandRequester<HttpRequest, HttpResponse> {

    private final HttpRequester httpRequester;

    public HttpCommandHandler(HttpRequester httpRequester) {
        this.httpRequester = httpRequester;
    }

    @Override
    protected HttpResponse execute(HttpRequest httpRequest) {
        return httpRequester.execute(httpRequest);
    }
}