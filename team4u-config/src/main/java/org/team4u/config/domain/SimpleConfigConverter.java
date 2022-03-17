package org.team4u.config.domain;

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
     * @param toType     目标配置类型
     * @param configType 配置项类型或配置项前缀
     * @param <T>        目标配置类型
     * @return 目标配置类
     */
    <T> T to(Class<T> toType, String configType);

    /**
     * 根据特定的配置项转换为指定的配置类
     * <p>
     * 默认开启缓存结果
     *
     * @param toType     目标配置类型
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @param <T>        目标配置类型
     * @return 目标配置类
     */
    default <T> T to(Type toType, String configType, String configKey) {
        return to(toType, configType, configKey, true);
    }

    /**
     * 根据特定的配置项转换为指定的配置类
     * <p>
     * 默认缓存转换后的配置类，即相同的配置项
     *
     * @param toType        目标配置类型
     * @param configType    配置类型或配置前缀
     * @param configKey     配置项key
     * @param isCacheResult 是否缓存转换后的配置类。true则缓存，相同的配置项仅转换一次，仅不同时会再次转换和缓存；false为不缓存，每次重新转换
     * @param <T>           目标配置类型
     * @return 目标配置类
     */
    <T> T to(Type toType, String configType, String configKey, boolean isCacheResult);

    /**
     * 获取指定配置项对应的值
     *
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @return 配置值
     */
    String value(String configType, String configKey);

    /**
     * 获取所有配置项
     *
     * @return 配置项集合
     */
    SimpleConfigs allConfigs();
}