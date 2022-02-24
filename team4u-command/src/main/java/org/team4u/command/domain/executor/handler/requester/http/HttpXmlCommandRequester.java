package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.domain.executor.handler.requester.XmlExtractorHandler;

/**
 * 基于HTTP和XML的命令处理器
 * <p>
 * 将Xml返回值转换为命令响应
 *
 * @author jay.wu
 */
public abstract class HttpXmlCommandRequester extends HttpCommandRequester {

    protected HttpXmlCommandRequester(HttpBaseRequester httpRequester) {
        super(httpRequester);
    }

    @Override
    protected Object toCommandResponse(Context context, HttpRequester.HttpResponse response) {
        return new XmlExtractorHandler().extract(context, response.getBody());
    }
}