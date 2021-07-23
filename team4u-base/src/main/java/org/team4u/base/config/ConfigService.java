package org.team4u.base.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

/**
 * 配置服务
 *
 * @author jay.wu
 */
public interface ConfigService {

    /**
     * 获取配置值
     */
    String get(String key);

    /**
     * 获取配置值
     *
     * @param defaultValue 默认值，不能为null
     */
    @SuppressWarnings("unchecked")
    default <T> T get(String key, T defaultValue) {
        T value = Convert.convert((Class<T>) defaultValue.getClass(), get(key));
        return ObjectUtil.defaultIfNull(value, defaultValue);
    }
}