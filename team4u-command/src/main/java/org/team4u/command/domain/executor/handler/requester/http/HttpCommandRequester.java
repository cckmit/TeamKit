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

    private final HttpBaseRequester httpRequester;

    public HttpCommandRequester(HttpBaseRequester httpRequester) {
        this.httpRequester = httpRequester;
    }

    @Override
    protected HttpRequester.HttpResponse execute(HttpRequester.HttpRequest httpRequest) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "execute");
        lm.config().setMinSpendTimeMillsToDisplay(0);

        try {
            HttpRequester.HttpResponse httpResponse = httpRequester.execute(httpRequest);

            if (log.isDebugEnabled()) {
                // debug模式下输出完整信息
                lm.append("request", httpRequest).append("response", httpResponse);
            } else {
                // 其余模式输出精简信息，防止打印敏感信息
                lm.append("request", httpRequest.getUrl()).append("response", httpResponse.getStatus());
            }

            log.info(lm.success().toString());
            return httpResponse;
        } catch (Exception e) {
            log.info(lm.fail().toString());
            throw e;
        }
    }
}