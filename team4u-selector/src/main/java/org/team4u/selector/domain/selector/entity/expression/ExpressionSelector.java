package org.team4u.selector.domain.selector.entity.expression;

import cn.hutool.core.convert.Convert;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.entity.binding.SingleValueBinding;
import org.team4u.template.TemplateEngine;

import java.util.Map;

/**
 * 表达式选择器
 *
 * @author jay.wu
 */
public class ExpressionSelector implements Selector {

    private TemplateEngine templateEngine;
    private Map<String, String> valueExpressions;

    /**
     * 构建表达式选择器
     *
     * @param templateEngine   模板引擎，用于解析表达式
     * @param valueExpressions 结果/表达式映射集合
     *                         * 表达式格式为${...}，如${x == 1}，其中x通过context传入
     *                         * 表达式返回值必须String支，持的值为：true、false、yes、ok、no，1,0
     */
    public ExpressionSelector(TemplateEngine templateEngine, Map<String, String> valueExpressions) {
        this.templateEngine = templateEngine;
        this.valueExpressions = valueExpressions;
    }

    @Override
    public String select(SelectorBinding binding) {
        SimpleMapBinding newBinding = new SimpleMapBinding();
        if (binding != null) {
            if (binding instanceof SingleValueBinding) {
                newBinding.set(SingleValueBinding.KEY, ((SingleValueBinding) binding).value());
            } else if (binding instanceof SimpleMapBinding) {
                newBinding.putAll((SimpleMapBinding) binding);
            }
        }

        for (Map.Entry<String, String> entry : valueExpressions.entrySet()) {
            // 使用上下文构建最终结果
            String value = templateEngine.render(entry.getValue(), newBinding);
            // 返回结果必须满足类布尔形式
            if (Convert.toBool(value, false)) {
                return entry.getKey();
            }
        }

        // 若无法匹配则返回空字符
        return NONE;
    }
}