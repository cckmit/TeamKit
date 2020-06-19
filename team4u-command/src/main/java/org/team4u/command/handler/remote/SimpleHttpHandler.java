package org.team4u.command.handler.remote;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.core.error.RemoteCallException;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

import java.util.Map;

/**
 * http处理器
 *
 * @author jay.wu
 */
public class SimpleHttpHandler extends AbstractDefaultHandler<HttpConfig, SimpleHttpResponse>
        implements RemoteHandler {

    public SimpleHttpHandler(TemplateEngine templateEngine,
                             HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    private HttpRequest toHttpRequest(HttpConfig request) {
        HttpRequest httpRequest = new HttpRequest(request.getUrl())
                .setMethod(Method.valueOf(request.getMethod()))
                .setConnectionTimeout(request.getConnectTimeoutMillis())
                .setReadTimeout(request.getReadTimeoutMillis())
                .body(request.getBody())
                .form(request.getParams());

        for (Map.Entry<String, Object> entry : request.getHeaders().entrySet()) {
            httpRequest.header(entry.getKey(), Convert.toStr(entry.getValue()));
        }

        return httpRequest;
    }

    @Override
    protected SimpleHttpResponse internalHandle(HttpConfig config, EasyMap attributes) {
        HttpRequest httpRequest = toHttpRequest(config);

        try {
            HttpResponse resp = httpRequest.execute();
            return new SimpleHttpResponse(resp.getStatus(), resp.body());
        } catch (Exception e) {
            throw new RemoteCallException(e);
        }
    }

    @Override
    public String id() {
        return "remoteHttp";
    }
}