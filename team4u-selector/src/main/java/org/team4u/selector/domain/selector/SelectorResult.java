package org.team4u.selector.domain.selector;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * 选择器结果
 *
 * @author jay.wu
 */
@Data
public class SelectorResult {
    /**
     * 不匹配结果
     */
    public static final SelectorResult NOT_MATCH = new SelectorResult(null);
    /**
     * 最终选择结果
     */
    private final Object value;

    private SelectorResult(Object value) {
        this.value = value;
    }

    /**
     * 根据原始值重建结果对象
     *
     * @param value 原始值
     * @return 选择器结果
     */
    public static SelectorResult valueOf(Object value) {
        return new SelectorResult(value);
    }

    /**
     * 将原始值转换为指定类型
     *
     * @param type 目标类型
     * @param <T>  目标类型
     * @return 转换后对象
     */
    public <T> T to(Type type) {
        return Convert.convert(type, value);
    }

    /**
     * 是否匹配
     *
     * @return 当value为null或者为false是返回不匹配，其他情况返回匹配
     */
    public boolean isMatch() {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return Convert.toBool(value);
        }

        return true;
    }

    /**
     * 是否未匹配
     */
    public boolean isNotMatch() {
        return !isMatch();
    }

    /**
     * 将最终选择结果转换为字符串
     */
    @Override
    public String toString() {
        return to(String.class);
    }
}