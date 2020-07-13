package org.team4u.command.handler.render;

import cn.hutool.core.lang.Console;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 控制台渲染器
 *
 * @author jay.wu
 */
public class ConsoleRender extends AbstractDefaultHandler<HandlerConfig, Void> {

    public ConsoleRender(TemplateEngine templateEngine,
                         HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected Void internalHandle(HandlerConfig config, EasyMap attributes) {
        Console.print(sourceOf(config, attributes, null));
        return null;
    }

    @Override
    public String id() {
        return "consoleRender";
    }
}