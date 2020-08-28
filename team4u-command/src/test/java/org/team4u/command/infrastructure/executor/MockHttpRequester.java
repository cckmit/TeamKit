package org.team4u.command.infrastructure.executor;

import cn.hutool.http.HttpStatus;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequester;

public class MockHttpRequester implements HttpRequester {

    @Override
    public HttpResponse execute(HttpRequest request) {
        return new HttpResponse(HttpStatus.HTTP_OK, request.getBody());
    }
}