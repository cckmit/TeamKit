package org.team4u.command.infrastructure.handler.http;

import cn.hutool.http.webservice.SoapClient;
import org.team4u.command.domain.executor.handler.requester.http.SoapRequester;

/**
 * 基于SoapClient的请求者
 *
 * @author jay.wu
 */
public class SimpleSoapRequester implements SoapRequester {

    @Override
    public HttpResponse execute(HttpRequest request) {
        SoapClient client = SoapClient.create(request.getUrl())
                .setConnectionTimeout(request.getConnectTimeoutMillis())
                .setReadTimeout(request.getReadTimeoutMillis())
                .setMethod(request.getMethod(), request.getExt().getStr(NAMESPACE_KEY))
                .setParams(request.getParams());
        cn.hutool.http.HttpResponse httpResponse = client.sendForResponse();
        return new HttpResponse(httpResponse.getStatus(), httpResponse.body());
    }
}