package org.team4u.base.data;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 表达式工具类
 *
 * @author jay.wu
 */
public class ExpressionUtil {

    /**
     * 将集合表达式[] 转换为具体的表达式[i]
     *
     * @param bean       目标集合
     * @param expression 原始集合表达式，包含[]
     * @return 转换后的表达式，若不包含集合表达式，则返回null；若包含则返回具体表达式，如[0],[1]，具体数量与目标集合一致
     */
    public static List<String> normalizeListExpression(Object bean, String expression) {
        int x = expression.indexOf("[]");

        if (x == -1) {
            return null;
        }

        String leftExpression = ObjectUtil.defaultIfEmpty(StrUtil.subPre(expression, x),".");
        String rightExpression = StrUtil.subSuf(expression, x + 2);

        Object listInBean = BeanUtil.getProperty(bean, leftExpression);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < ObjectUtil.length(listInBean); i++) {
            String newExpression = leftExpression + "[" + i + "]" + rightExpression;
            result.add(newExpression);
        }

        return result;
    }
}
