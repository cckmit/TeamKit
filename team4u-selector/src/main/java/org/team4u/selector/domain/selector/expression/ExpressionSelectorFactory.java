package org.team4u.selector.domain.selector.expression;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.registrar.NoSuchPolicyException;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.template.TemplateEngine;
import org.team4u.template.TemplateEngines;

import java.util.Map;

/**
 * 表达式选择器构建工厂
 *
 * @author jay.wu
 */
public class ExpressionSelectorFactory extends AbstractSelectorFactoryFactory<Map<String, String>> {

    private static final String TEMPLATE_ID_KEY = "template";
    private final TemplateEngines engines = new TemplateEngines();

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