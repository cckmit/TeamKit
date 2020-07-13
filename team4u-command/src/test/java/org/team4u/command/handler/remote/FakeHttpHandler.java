package org.team4u.command.handler.remote;

import cn.hutool.core.lang.Dict;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.TestUtil;
import org.team4u.base.lang.EasyMap;

public class FakeHttpHandler extends MockHttpHandler {

    private SimpleHttpResponse simpleHttpResponse;

    public FakeHttpHandler(HandlerInterceptorService interceptorService) {
        super(TestUtil.newTemplateEngine(), interceptorService);
    }

    @Override
    protected SimpleHttpResponse internalHandle(Dict config, EasyMap attributes) {
        return simpleHttpResponse;
    }

    @Override
    public String id() {
        return "remoteHttp";
    }

    public SimpleHttpResponse getSimpleHttpResponse() {
        return simpleHttpResponse;
    }

    public FakeHttpHandler setSimpleHttpResponse(SimpleHttpResponse simpleHttpResponse) {
        this.simpleHttpResponse = simpleHttpResponse;
        return this;
    }
}