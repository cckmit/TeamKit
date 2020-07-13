package org.team4u.command.handler.extract;

import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * Xml提取处理器
 *
 * @author jay.wu
 */
public class XmlExtractHandler extends JsonExtractHandler {

    public XmlExtractHandler(TemplateEngine templateEngine,
                             HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T sourceOf(ExtractConfig config, EasyMap attributes, Class<T> sourceType) {
        Object json = super.sourceOf(config, attributes, sourceType);
        return (T) XmlUtil.xmlToMap(JSONUtil.toJsonStr(json));
    }

    @Override
    protected Class<ExtractConfig> configType() {
        return ExtractConfig.class;
    }

    @Override
    public String id() {
        return "xmlExtractor";
    }
}