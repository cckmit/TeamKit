package org.team4u.template.infrastructure.function;


import org.team4u.template.TemplateFunction;

/**
 * 常用数值模板函数
 *
 * @author jay.wu
 */
public class NumberTemplateFunction implements TemplateFunction {

    @Override
    public String id() {
        return "n";
    }

    /**
     * 当前时间戳
     */
    public static long now() {
        return System.currentTimeMillis();
    }
}