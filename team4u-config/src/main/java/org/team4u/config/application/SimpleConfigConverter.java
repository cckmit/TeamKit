package org.team4u.config.application;

import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 配置转换器
 *
 * @author jay.wu
 */
public interface SimpleConfigConverter {

    /**
     * 将多个配置项转换为指定的配置类
     *
     * @param <T>        目标配置类型
     * @param configType 配置项类型或配置项前缀
     * @param toType     目标配置类型
     * @return 目标配置类
     */
    <T> T to(String configType, Class<T> toType);

    /**
     * 根据特定的配置项转换为指定的配置类
     *
     * @param <T>        目标配置类型
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @param toType     目标配置类型
     * @return 目标配置类
     */
    <T> T to(String configType, String configKey, Type toType);

    /**
     * 获取指定配置项对应的值
     *
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @return 配置值
     */
    default String value(String configType, String configKey) {
        return allConfigs().value(configType, configKey);
    }

    /**
     * 获取所有配置项
     *
     * @return 配置项集合
     */
    SimpleConfigs allConfigs();
}