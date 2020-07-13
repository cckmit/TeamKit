package org.team4u.base.data.setter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Replacer;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import org.team4u.base.data.ExpressionUtil;

import java.util.List;

/**
 * 对象属性值替换器
 *
 * @author jay.wu
 */
public class BeanPropertyValueReplacer {

    /**
     * 根据属性表达式替换指定属性值
     *
     * @param bean       待赋值对象
     * @param expression 对象属性表达式
     * @param replacer   替换器
     */
    public void replace(Object bean, String expression, Replacer<Object> replacer) {
        if (ObjectUtil.isEmpty(bean)) {
            return;
        }

        if (ClassUtil.isSimpleValueType(bean.getClass())) {
            return;
        }

        if (expression.contains("[]")) {
            replaceListProperty(bean, expression, replacer);
        } else {
            replaceDirectProperty(bean, expression, replacer);
        }
    }

    /**
     * 根据属性表达式直接替换属性值
     */
    private void replaceDirectProperty(Object bean, String expression, Replacer<Object> replacer) {
        Object value;

        try {
            value = BeanUtil.getProperty(bean, expression);

            if (value == null) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        BeanUtil.setProperty(bean, expression, replacer.replace(value));
    }

    /**
     * 根据属性表达式替换包含动态集合的属性值
     */
    private void replaceListProperty(Object bean, String expression, Replacer<Object> replacer) {
        List<String> expressions = ExpressionUtil.normalizeListExpression(bean, expression);

        if (CollUtil.isEmpty(expressions)) {
            return;
        }

        for (String newExpression : expressions) {
            replace(bean, newExpression, replacer);
        }
    }
}