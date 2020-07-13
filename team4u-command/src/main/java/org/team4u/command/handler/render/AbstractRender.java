package org.team4u.command.handler.render;

import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 控制台渲染器
 *
 * @author jay.wu
 */
public abstract class AbstractRender<Config> extends AbstractDefaultHandler<Config, Void> {

    public AbstractRender(TemplateEngine templateEngine,
                          HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected Void internalHandle(Config config, EasyMap attributes) {
        render(config, attributes);
        return null;
    }


    protected abstract void render(Config config, EasyMap attributes);
}