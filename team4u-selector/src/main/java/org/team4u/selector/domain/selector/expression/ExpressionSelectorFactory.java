package org.team4u.selector.domain.selector.expression;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.base.registrar.NoSuchPolicyException;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateEngines;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表达式选择器构建工厂
 *
 * @author jay.wu
 */
public class ExpressionSelectorFactory extends AbstractSelectorFactoryFactory<Map<String, String>> {

    private static final String TEMPLATE_ID_KEY = "template";
    private final TemplateEngines engines = new TemplateEngines();

    @Override
    public Map<String, String> toConfig(String jsonConfig) {
        return JSONUtil.parseObj(jsonConfig)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        it -> Convert.toStr(it.getValue())
                ));
    }

    @Override
    public String id() {
        return "expression";
    }

    @Override
    protected Selector createWithConfig(Map<String, String> config) {
        return new ExpressionSelector(templateEngineOf(config.get(TEMPLATE_ID_KEY)), config);
    }

    private TemplateEngine templateEngineOf(String templateId) {
        if (StrUtil.isNotBlank(templateId)) {
            return engines.availablePolicyOf(templateId);
        }

        return engines.policies()
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchPolicyException("Unable to find any available templateEngine"));
    }
}