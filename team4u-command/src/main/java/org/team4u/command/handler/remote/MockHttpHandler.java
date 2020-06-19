package org.team4u.command.handler.remote;

import cn.hutool.core.lang.Dict;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 模拟http处理器
 * <p>
 * 仅用于测试，直接配置结果
 *
 * @author jay.wu
 */
public class MockHttpHandler extends AbstractDefaultHandler<Dict, SimpleHttpResponse> implements RemoteHandler {

    public MockHttpHandler(TemplateEngine templateEngine,
                           HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected SimpleHttpResponse internalHandle(Dict config, EasyMap attributes) {
        return config.toBean(SimpleHttpResponse.class);
    }

    @Override
    public String id() {
        return "mockHttp";
    }
}
