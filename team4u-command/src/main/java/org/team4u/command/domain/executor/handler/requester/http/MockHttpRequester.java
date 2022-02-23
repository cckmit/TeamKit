package org.team4u.command.domain.executor.handler.requester.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpStatus;
import lombok.Getter;

/**
 * 模拟HTTP的请求者
 *
 * @author jay.wu
 */
@Getter
public class MockHttpRequester implements HttpRequester {

    private final int status;
    private final String body;

    public MockHttpRequester(String body) {
        this(HttpStatus.HTTP_OK, body);
    }

    public MockHttpRequester(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public static MockHttpRequester requesterOfPath(String path) {
        return new MockHttpRequester(FileUtil.readUtf8String(path));
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        return new HttpResponse(status, body);
    }
}