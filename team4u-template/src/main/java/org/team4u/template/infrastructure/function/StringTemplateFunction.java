package org.team4u.template.infrastructure.function;

import cn.hutool.crypto.SecureUtil;
import org.team4u.template.TemplateFunction;

/**
 * 常用字符串模板函数
 */
public class StringTemplateFunction implements TemplateFunction {

    @Override
    public String id() {
        return "s";
    }

    /**
     * 大写md5值
     */
    public String md5u(String value) {
        return SecureUtil.md5(value).toUpperCase();
    }

    /**
     * 小写md5值
     */
    public String md5l(String value) {
        return md5u(value).toLowerCase();
    }
}