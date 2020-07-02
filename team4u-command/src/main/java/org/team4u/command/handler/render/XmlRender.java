package org.team4u.command.handler.render;

import cn.hutool.core.util.XmlUtil;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.command.handler.HandlerConfig;
import org.team4u.core.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * xml渲染器
 *
 * @author jay.wu
 */
public class XmlRender extends AbstractDefaultHandler<HandlerConfig, String> {

    public XmlRender(TemplateEngine templateEngine,
                     HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @Override
    protected String internalHandle(HandlerConfig config, EasyMap attributes) {
        Object source = sourceOf(config, attributes, null);
        return XmlUtil.toStr(XmlUtil.beanToXml(source));
    }

    @Override
    public String id() {
        return "xmlRender";
    }
}