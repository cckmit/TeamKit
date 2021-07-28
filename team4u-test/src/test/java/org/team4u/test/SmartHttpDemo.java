package org.team4u.test;

import cn.hutool.json.JSONUtil;
import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandler;
import org.smartboot.http.server.handler.HttpRouteHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SmartHttpDemo {

    public static void main(String[] args) {
        HttpRouteHandler routeHandler = new HttpRouteHandler();
        routeHandler.route("/", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
                httpResponse.write("1234".getBytes(StandardCharsets.UTF_8));
            }
        }).route("/a", new HttpServerHandler() {
            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
                httpResponse.write(JSONUtil.toJsonStr(httpRequest.getParameters()).getBytes(StandardCharsets.UTF_8));
            }
        });
        new HttpBootstrap().httpHandler(routeHandler).setPort(8080).start();
    }
}