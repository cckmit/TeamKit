package org.team4u.command.infrastructure.handler.http;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.Method;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequest;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequester;
import org.team4u.command.domain.executor.handler.requester.http.HttpResponse;
import org.team4u.base.error.RemoteCallException;

import java.util.Map;

public class SimpleHttpRequester implements HttpRequester {

    @Override
    public HttpResponse execute(HttpRequest request) {
        cn.hutool.http.HttpRequest httpRequest = toHttpRequest(request);

        try {
            cn.hutool.http.HttpResponse resp = httpRequest.execute();
            return new HttpResponse(resp.getStatus(), resp.body());
        } catch (Exception e) {
            throw new RemoteCallException(e);
        }
    }

    private cn.hutool.http.HttpRequest toHttpRequest(HttpRequest request) {
        cn.hutool.http.HttpRequest httpRequest = new cn.hutool.http.HttpRequest(request.getUrl())
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

}