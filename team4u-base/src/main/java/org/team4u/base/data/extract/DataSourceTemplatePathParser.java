package org.team4u.base.data.extract;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源模板路径解析器
 *
 * @author jay.wu
 */
public class DataSourceTemplatePathParser {

    /**
     * 解析路径集合
     */
    public Map<String, List<String>> parseTemplatePathValues(Object template) {
        Map<String, List<String>> templatePathValues = new HashMap<>();
        parseTemplatePathValues(template, templatePathValues, "");
        return templatePathValues;
    }

    /**
     * 解析路径集合
     */
    public void parseTemplatePathValues(Object template,
                                        Map<String, List<String>> templatePathValues,
                                        String path) {
        Assert.notNull(template);

        if (template instanceof Map) {
            parseMapTemplatePathValues(templatePathValues, (Map<?, ?>) template, path);
        } else if (template instanceof List) {
            parseListTemplatePathValues(templatePathValues, (Collection<?>) template, path);
        }
    }

    private void parseMapTemplatePathValues(Map<String, List<String>> templatePathValues,
                                            Map<?, ?> template,
                                            String path) {
        for (Map.Entry<?, ?> entry : template.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();

            String newPath = normalizePath(path) + key;

            if (value instanceof String) {
                templatePathValues.put(newPath, CollUtil.newArrayList(value.toString()));
                continue;
            }

            parseTemplatePathValues(value, templatePathValues, newPath);
        }
    }

    private String normalizePath(String path) {
        return path.equals("") ? path : path + ".";
    }

    private void parseListTemplatePathValues(Map<String, List<String>> templatePathValues,
                                             Collection<?> template,
                                             String path) {
        if (CollUtil.isEmpty(template)) {
            return;
        }

        Object value = CollUtil.getFirst(template);

        if (value instanceof String) {
            templatePathValues.put(
                    path,
                    template
                            .stream()
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.toList())
            );
            return;
        }

        parseTemplatePathValues(value, templatePathValues, path + "[]");
    }
}