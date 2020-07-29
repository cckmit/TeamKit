package org.team4u.command.infrastructure.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.team4u.base.data.extract.DataTargetTemplateExtracter;
import org.team4u.base.data.extract.FastJsonDataTargetTemplateSerializer;

import java.util.Map;

public class JsonExtractor {

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

    private Object extractableSource(Object source) {
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

    public static class ExtractConfig {

        /**
         * 提取模板
         */
        private String template;
        /**
         * 目标类型
         */
        private String targetType;

        public String getTemplate() {
            return template;
        }

        public ExtractConfig setTemplate(String template) {
            this.template = template;
            return this;
        }

        public String getTargetType() {
            return targetType;
        }

        public ExtractConfig setTargetType(String targetType) {
            this.targetType = targetType;
            return this;
        }
    }
}