package org.team4u.command.domain.executor.handler.requester.http;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

/**
 * 基于HTTP的命令处理器
 *
 * @author jay.wu
 */
public abstract class HttpCommandRequester extends CommandRequester<HttpRequester.HttpRequest, HttpRequester.HttpResponse> {

    protected final Log log = Log.get();

    private final HttpRequester httpRequester;

    public HttpCommandRequester(HttpRequester httpRequester) {
        this.httpRequester = httpRequester;
    }

    @Override
    protected HttpRequester.HttpResponse execute(HttpRequester.HttpRequest httpRequest) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "execute")
                .append("request", httpRequest);

        HttpRequester.HttpResponse response = httpRequester.execute(httpRequest);

        lm.append("response", response);

        if (log.isDebugEnabled()) {
            log.debug(lm.toString());
        }

        return response;
    }
}