package org.team4u.selector.domain.selector.expression;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.base.bean.provider.BeanProviders;
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
public class ExpressionSelectorFactory extends AbstractSelectorFactoryFactory<Map<String, String>> {

    @Override
    public Map<String, String> toConfig(String jsonConfig) {
        return JSONUtil.parseArray(jsonConfig)
                .stream()
                .flatMap(it -> ((JSONObject) it).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, it -> Convert.toStr(it.getValue())));
    }

    @Override
    public String id() {
        return "expression";
    }

    @Override
    protected Selector createWithConfig(Map<String, String> config) {
        TemplateEngine templateEngine = BeanProviders.getInstance().getBean(TemplateEngine.class);
        return new ExpressionSelector(templateEngine, config);
    }
}