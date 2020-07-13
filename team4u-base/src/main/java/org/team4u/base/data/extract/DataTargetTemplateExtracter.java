package org.team4u.base.data.extract;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Replacer;
import org.team4u.base.data.ExpressionUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于目标类模板的提取器
 *
 * @author jay.wu
 */
public class DataTargetTemplateExtracter {

    private final DataTargetTemplateSerializer dataTargetTemplateSerializer;

    public DataTargetTemplateExtracter(DataTargetTemplateSerializer dataTargetTemplateSerializer) {
        this.dataTargetTemplateSerializer = dataTargetTemplateSerializer;
    }

    /**
     * 提取到bean
     *
     * @param template   目标值模板
     * @param source     源值
     * @param targetType 目标类型
     * @return 目标值
     */
    public <T> T extractToBean(String template, Object source, Class<T> targetType) {
        return dataTargetTemplateSerializer.deserializeToBean(extract(template, source), targetType);
    }

    /**
     * 提取到List
     *
     * @param template   目标值模板
     * @param source     源值
     * @param targetType 目标集合元素类型
     * @return 目标值
     */
    public <T> List<T> extractToList(String template, Object source, Class<T> targetType) {
        return dataTargetTemplateSerializer.deserializeToList(extract(template, source), targetType);
    }

    private Object extract(String template, Object source) {
        Object replaceableObject = dataTargetTemplateSerializer.serializeToReplaceableObject(template);

        return new LeafReplacer().replace(replaceableObject, o -> {
            // 非表达式，为固定值，直接返回
            if (!(o instanceof String)) {
                return o;
            }

            return extractPropertyValue(source, (String) o);
        });
    }

    /**
     * 提取指定表达式的属性值
     *
     * @param value      目标值
     * @param expression 表达式
     */
    private Object extractPropertyValue(Object value, String expression) {
        List<Object> values = extractDynamicListPropertyValue(value, expression);

        if (values != null) {
            return values;
        }

        return extractBeanPropertyValue(value, expression);
    }

    /**
     * 提取标准bean属性值
     */
    private Object extractBeanPropertyValue(Object bean, String expression) {
        return BeanUtil.getProperty(bean, expression);
    }

    /**
     * 提取动态List属性值
     *
     * @param expression 包含[]的属性动态List表达式，如a[]
     */
    private List<Object> extractDynamicListPropertyValue(Object bean, String expression) {
        List<String> expressions = ExpressionUtil.normalizeListExpression(bean, expression);

        if (expressions == null) {
            return null;
        }

        return expressions.stream()
                .map(it -> BeanUtil.getProperty(bean, it))
                .collect(Collectors.toList());
    }

    /**
     * 叶子节点替换器
     */
    private static class LeafReplacer {

        /**
         * 替换叶子节点值
         *
         * @param value    替换前的原始值
         * @param replacer 替换器
         * @return 替换后的修改值
         */
        public Object replace(Object value, Replacer<Object> replacer) {
            if (value == null) {
                return null;
            }

            if (value instanceof Map) {
                replaceForMap((Map<?, ?>) value, replacer);
                return value;
            }

            if (value instanceof List) {
                replaceForList((List<?>) value, replacer);
                return value;
            }

            return replacer.replace(value);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private void replaceForList(List value, Replacer<Object> replacer) {
            for (int i = 0; i < value.size(); i++) {
                Object v = replace(value.get(i), replacer);
                value.set(i, v);
            }
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private void replaceForMap(Map<?, ?> value, Replacer<Object> replacer) {
            for (Map.Entry entry : value.entrySet()) {
                Object v = replace(entry.getValue(), replacer);
                entry.setValue(v);
            }
        }
    }
}