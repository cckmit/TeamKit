package org.team4u.base.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import java.util.Map;

/**
 * 配置服务
 *
 * @author jay.wu
 */
public interface ConfigService {

    /**
     * 获取配置值
     *
     * @param key 配置key值
     */
    String get(String key);

    /**
     * 获取配置值
     *
     * @param key          配置key
     * @param defaultValue 默认值，能为null
     */
    @SuppressWarnings("unchecked")
    default <T> T get(String key, T defaultValue) {
        if (defaultValue == null) {
            return (T) get(key);
        }

        return (T) ObjectUtil.defaultIfNull(get(defaultValue.getClass(), key), defaultValue);
    }

    /**
     * 获取配置值
     *
     * @param key       配置key
     * @param classType 值类型
     */
    default <T> T get(Class<T> classType, String key) {
        return Convert.convert(classType, get(key));
    }

    /**
     * 获取所有配置项
     *
     * @return 配置项集合
     */
    Map<String, Object> allConfigs();
}