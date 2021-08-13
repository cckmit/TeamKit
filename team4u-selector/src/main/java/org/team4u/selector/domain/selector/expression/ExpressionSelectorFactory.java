package org.team4u.selector.domain.selector.expression;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.template.TemplateEngine;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表达式选择器构建工厂
 *
 * @author jay.wu
 */
public class ExpressionSelectorFactory extends AbstractSelectorFactoryFactory {

    private final TemplateEngine templateEngine;

    public ExpressionSelectorFactory(TemplateEngine templateEngine) {
        this(CacheUtil.newLRUCache(1000), templateEngine);
    }

    public ExpressionSelectorFactory(Cache<String, Selector> cache, TemplateEngine templateEngine) {
        super(cache);
        this.templateEngine = templateEngine;
    }

    public static Map<String, String> toConfig(String jsonConfig) {
        return JSONUtil.parseArray(jsonConfig)
                .stream()
                .flatMap(it -> ((JSONObject) it).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, it -> Convert.toStr(it.getValue())));
    }

    @Override
    public Selector call(String jsonConfig) {
        return new ExpressionSelector(templateEngine, toConfig(jsonConfig));
    }

    @Override
    public String id() {
        return "expression";
    }
}