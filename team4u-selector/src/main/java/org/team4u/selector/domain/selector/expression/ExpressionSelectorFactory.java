package org.team4u.selector.domain.selector.expression;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.template.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

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
        JSONArray config = JSONUtil.parseArray(jsonConfig);
        Map<String, String> valueExpressions = new HashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                valueExpressions.put(w.getKey(), Convert.toStr(w.getValue()));
            }
        }

        return valueExpressions;
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