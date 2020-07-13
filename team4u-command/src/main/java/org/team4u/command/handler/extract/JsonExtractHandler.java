package org.team4u.command.handler.extract;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.handler.AbstractDefaultHandler;
import org.team4u.base.data.extract.DataTargetTemplateExtracter;
import org.team4u.base.data.extract.FastJsonDataTargetTemplateSerializer;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

import java.util.Map;

/**
 * Json提取处理器
 *
 * @author jay.wu
 */
public class JsonExtractHandler extends AbstractDefaultHandler<ExtractConfig, Object> {

    public JsonExtractHandler(TemplateEngine templateEngine,
                              HandlerInterceptorService handlerInterceptorService) {
        super(templateEngine, handlerInterceptorService);
    }


    @Override
    protected Object internalHandle(ExtractConfig config, EasyMap attributes) {
        return toTarget(
                config.getTemplate(),
                extractableSource(config, attributes),
                targetType(config)
        );
    }

    private Class<?> targetType(ExtractConfig extractConfig) {
        String targetType = extractConfig.getTargetType();
        if (StrUtil.isEmpty(targetType)) {
            return Map.class;
        }

        return ClassUtil.loadClass(targetType);
    }

    private Object extractableSource(ExtractConfig config, EasyMap attributes) {
        Object source = sourceOf(config, attributes, null);

        if (source instanceof String) {
            return JSON.parseObject((String) source);
        }

        return source;
    }

    private Object toTarget(String template, Object source, Class<?> targetClass) {
        return new DataTargetTemplateExtracter(new FastJsonDataTargetTemplateSerializer())
                .extractToBean(
                        template,
                        source,
                        targetClass
                );
    }

    @Override
    public String id() {
        return "jsonExtractor";
    }
}