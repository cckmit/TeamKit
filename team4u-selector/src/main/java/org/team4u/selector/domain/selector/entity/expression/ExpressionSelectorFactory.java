package org.team4u.selector.domain.selector.entity.expression;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.SelectorFactory;
import org.team4u.template.TemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * 表达式选择器构建工厂
 *
 * @author jay.wu
 */
public class ExpressionSelectorFactory implements SelectorFactory {

    private TemplateEngine templateEngine;

    public ExpressionSelectorFactory(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Selector create(String jsonConfig) {
        JSONArray config = JSONUtil.parseArray(jsonConfig);
        Map<String, String> valueExpressions = new HashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                valueExpressions.put(w.getKey(), Convert.toStr(w.getValue()));
            }
        }

        return new ExpressionSelector(templateEngine, valueExpressions);
    }

    @Override
    public String id() {
        return "expression";
    }
}