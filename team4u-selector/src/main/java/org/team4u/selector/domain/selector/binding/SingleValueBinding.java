package org.team4u.selector.domain.selector.binding;

import cn.hutool.core.convert.Convert;

/**
 * 单值绑定参数
 *
 * @author jay.wu
 */
public class SingleValueBinding implements SelectorBinding {

    public static final String KEY = "it";

    private final String value;

    public SingleValueBinding(Object value) {
        this.value = Convert.toStr(value);
    }

    public String value() {
        return value;
    }
}