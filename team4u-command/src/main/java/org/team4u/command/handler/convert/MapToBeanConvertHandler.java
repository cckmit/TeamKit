package org.team4u.command.handler.convert;

import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

public class MapToBeanConvertHandler extends AbstractDefaultHandler<ConvertConfig, Object> {

    public MapToBeanConvertHandler(TemplateEngine templateEngine, HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected Object internalHandle(ConvertConfig convertConfig, EasyMap attributes) {
        return sourceOf(convertConfig, attributes, convertConfig.loadTargetType());
    }

    @Override
    public String id() {
        return "beanConverter";
    }
}