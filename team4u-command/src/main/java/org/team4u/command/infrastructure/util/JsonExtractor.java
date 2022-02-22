package org.team4u.command.infrastructure.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.team4u.base.data.extract.DataTargetTemplateExtracter;
import org.team4u.base.data.extract.FastJsonDataTargetTemplateSerializer;
import org.team4u.base.lang.lazy.LazySupplier;

import java.util.Map;

/**
 * Json提取器
 *
 * @author jay.wu
 */
public class JsonExtractor {

    private static final LazySupplier<JsonExtractor> instance = LazySupplier.of(JsonExtractor::new);

    public static JsonExtractor getInstance() {
        return instance.get();
    }

    @SuppressWarnings("unchecked")
    public <T> T extract(ExtractConfig config, Object source) {
        return (T) toTarget(
                config.getTemplate(),
                extractableSource(source),
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

    protected Object extractableSource(Object source) {
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

}