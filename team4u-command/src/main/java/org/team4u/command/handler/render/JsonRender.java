package org.team4u.command.handler.render;

import com.alibaba.fastjson.JSON;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * json渲染器
 *
 * @author jay.wu
 */
public class JsonRender extends AbstractDefaultHandler<HandlerConfig, String> {

    public JsonRender(TemplateEngine templateEngine,
                      HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected String internalHandle(HandlerConfig config, EasyMap attributes) {
        Object source = sourceOf(config, attributes, null);
        return JSON.toJSONString(source);
    }

    @Override
    public String id() {
        return "jsonRender";
    }
}