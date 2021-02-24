package org.team4u.test;

import org.smartboot.http.server.*;
import org.smartboot.http.server.handle.HttpRouteHandle;
import org.smartboot.http.server.handle.WebSocketDefaultHandle;
import org.smartboot.http.server.handle.WebSocketRouteHandle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class SmartHttpDemo {

    public static void main(String[] args) {
        System.setProperty("smartHttp.server.alias", "SANDAO base on ");

        HttpRouteHandle routeHandle = new HttpRouteHandle();
        routeHandle.route("/", new HttpServerHandle() {
            final byte[] body = ("<html>" +
                    "<head><title>smart-http demo</title></head>" +
                    "<body>" +
                    "GET 表单提交<form action='/get' method='get'><input type='text' name='text'/><input type='submit'/></form></br>" +
                    "POST 表单提交<form action='/post' method='post'><input type='text' name='text'/><input type='submit'/></form></br>" +
                    "文件上传<form action='/upload' method='post' enctype='multipart/form-data'><input type='file' name='text'/><input type='submit'/></form></br>" +
                    "</body></html>").getBytes();

            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {

                response.setContentLength(body.length);
                response.getOutputStream().write(body);
            }
        }).route("/get", new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                response.write(("收到Get参数text=" + request.getParameter("text")).getBytes());
            }
        }).route("/post", new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                response.write(("收到Post参数text=" + request.getParameter("text")).getBytes());
            }
        }).route("/upload", new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                InputStream in = request.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, len);
                }
                in.close();
            }
        }).route("/post_json", new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                InputStream in = request.getInputStream();
                byte[] buffer = new byte[1024];
                int len;
                System.out.println(request.getContentType());
                while ((len = in.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, len);
                }
                in.close();
            }
        }).route("/plaintext", new HttpServerHandle() {
            final byte[] body = "Hello World!".getBytes();

            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                response.setContentLength(body.length);
                response.setContentType("text/plain; charset=UTF-8");
                response.write(body);
            }
        }).route("/head", new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                response.addHeader("a", "b");
                response.addHeader("a", "c");
                Collection<String> headNames = request.getHeaderNames();
                for (String headerName : headNames) {
                    response.write((headerName + ": " + request.getHeaders(headerName) + "</br>").getBytes());
                }
            }
        });


        HttpBootstrap bootstrap = new HttpBootstrap();
        //配置HTTP消息处理管道
        bootstrap.pipeline().next(routeHandle);

        WebSocketRouteHandle wsRouteHandle = new WebSocketRouteHandle();
        wsRouteHandle.route("/ws", new WebSocketDefaultHandle() {
            @Override
            public void onHandShark(WebSocketRequest request, WebSocketResponse webSocketResponse) {
                System.out.println("收到握手消息");
            }

            @Override
            public void handleTextMessage(WebSocketRequest request, WebSocketResponse response, String data) {
                System.out.println("收到请求消息:" + data);
                response.sendTextMessage("服务端收到响应:" + data);
            }

            @Override
            public void onClose(WebSocketRequest request, WebSocketResponse response) {
                super.onClose(request, response);
            }

            @Override
            public void handleBinaryMessage(WebSocketRequest request, WebSocketResponse response, byte[] data) {
                response.sendBinaryMessage(data);
            }
        });
        bootstrap.wsPipeline().next(wsRouteHandle);

        //设定服务器配置并启动
        bootstrap.setPort(8080)
                .setReadBufferSize(4096)
                .setBufferPool(8 * 1024 * 1024,
                        Runtime.getRuntime().availableProcessors() + 2,
                        4096)
                .start();
    }
}