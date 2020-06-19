package org.team4u.command;

import cn.hutool.core.collection.CollUtil;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateFunction;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;

public class TestUtil {

    public static HandlerInterceptorService newInterceptorService(HandlerInterceptor... handlerInterceptors) {
        return new HandlerInterceptorService(CollUtil.toList(handlerInterceptors));
    }

    public static TemplateEngine newTemplateEngine(TemplateFunction... functions) {
        return new BeetlTemplateEngine(new TemplateFunctionService(CollUtil.toList(functions)));
    }
}
