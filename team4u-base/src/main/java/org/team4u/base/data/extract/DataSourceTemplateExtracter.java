package org.team4u.base.data.extract;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 基于来源类模板的提取器
 *
 * @author jay.wu
 */
public class DataSourceTemplateExtracter {

    /**
     * 模板路径集合
     */
    private final Map<String, List<String>> templatePathValues;

    public DataSourceTemplateExtracter(Object template) {
        templatePathValues = new DataSourceTemplatePathParser().parseTemplatePathValues(template);
    }

    /**
     * 提取到目标值
     *
     * @param source 源值
     * @param target 目标值
     */
    public void extract(Object source, Object target) {
        extract(source, target, new LinkedList<>(), new LinkedList<>());
    }

    private void extract(Object source,
                         Object target,
                         LinkedList<String> sourcePathNavigation,
                         LinkedList<Integer> indexNavigation) {
        if (source == null) {
            return;
        }

        if (ClassUtil.isSimpleValueType(source.getClass())) {
            return;
        }

        if (source instanceof Map) {
            extractForMap((Map<?, ?>) source, target, sourcePathNavigation, indexNavigation);
            return;
        }

        if (source instanceof Collection) {
            extractForList((Collection<?>) source, target, sourcePathNavigation, indexNavigation);
            return;
        }

        extractForMap(BeanUtil.beanToMap(source), target, sourcePathNavigation, indexNavigation);
    }

    private void extractForMap(Map<?, ?> source,
                               Object target,
                               LinkedList<String> sourcePathNavigation,
                               LinkedList<Integer> indexNavigation) {
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            sourcePathNavigation.addLast(entry.getKey().toString());

            extractByExpression(
                    currentExtractPath(sourcePathNavigation),
                    entry.getValue(),
                    target,
                    indexNavigation
            );

            extract(entry.getValue(), target, sourcePathNavigation, indexNavigation);
            sourcePathNavigation.removeLast();
        }
    }

    private void extractForList(Collection<?> source,
                                Object target,
                                LinkedList<String> sourcePathNavigation,
                                LinkedList<Integer> indexNavigation) {
        if (CollUtil.isEmpty(sourcePathNavigation)) {
            sourcePathNavigation.add("[]");
        } else {
            sourcePathNavigation.addLast(sourcePathNavigation.removeLast() + "[]");
        }

        for (int i = 0; i < source.size(); i++) {
            indexNavigation.addLast(i);
            extract(CollUtil.get(source, i), target, sourcePathNavigation, indexNavigation);
            indexNavigation.removeLast();
        }
    }

    private String currentExtractPath(List<String> sourcePaths) {
        return StrUtil.join(".", sourcePaths);
    }

    private void extractByExpression(String path,
                                     Object sourceValue,
                                     Object target,
                                     LinkedList<Integer> indexNavigation) {
        if (templatePathValues.containsKey(path)) {
            List<String> expressions = templatePathValues.get(path);

            for (String expression : expressions) {
                expression = normalizeExpression(expression, indexNavigation);
                BeanUtil.setProperty(target, ObjectUtil.defaultIfEmpty(expression, path), sourceValue);
            }
        }
    }

    private String normalizeExpression(String expression, LinkedList<Integer> indexNavigation) {
        int count = StrUtil.count(expression, "[]");
        if (count == 0) {
            return expression;
        }

        for (int i = count - 1; i > -1; i--) {
            int index = indexNavigation.get(indexNavigation.size() - i - 1);
            if (index < 0) {
                break;
            }

            expression = expression.replaceFirst("\\[]", String.format("[%d]", index));
        }

        return expression;
    }
}