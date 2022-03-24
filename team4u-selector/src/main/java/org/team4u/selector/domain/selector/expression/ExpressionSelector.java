package org.team4u.selector.domain.selector.expression;

import cn.hutool.core.convert.Convert;
import lombok.Getter;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;
import org.team4u.template.TemplateEngine;

import java.util.Map;

/**
 * 表达式选择器
 *
 * @author jay.wu
 */
public class ExpressionSelector implements Selector {

    @Getter
    private final TemplateEngine templateEngine;
    @Getter
    private final Map<String, String> config;

    /**
     * 构建表达式选择器
     *
     * @param templateEngine 模板引擎，用于解析表达式
     * @param config         表达式:结果映射集合
     *                       * 表达式格式为${...}，如${x == 1}，其中x通过binding传入
     *                       * 表达式返回值必须String，支持的值为：true、false、yes、ok、no、1、0
     */
    public ExpressionSelector(TemplateEngine templateEngine, Map<String, String> config) {
        this.templateEngine = templateEngine;
        this.config = config;
    }

    @Override
    public SelectorResult select(SelectorBinding binding) {
        SimpleMapBinding mapBinding = toMapBinding(binding);

        String result = config.entrySet()
                .stream()
                .filter(it -> isMatch(it.getKey(), mapBinding))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        return SelectorResult.valueOf(result);
    }

    private boolean isMatch(String key, SimpleMapBinding binding) {
        String value = templateEngine.render(key, binding);
        return Convert.toBool(value, false);
    }

    private SimpleMapBinding toMapBinding(SelectorBinding binding) {
        SimpleMapBinding newBinding = new SimpleMapBinding();

        if (binding == null) {
            return newBinding;
        }

        if (binding instanceof SingleValueBinding) {
            newBinding.set(SingleValueBinding.KEY, ((SingleValueBinding) binding).value());
            return newBinding;
        }

        if (binding instanceof SimpleMapBinding) {
            return (SimpleMapBinding) binding;
        }

        return newBinding;
    }
}