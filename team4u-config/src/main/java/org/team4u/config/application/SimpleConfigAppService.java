package org.team4u.config.application;

import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.converter.DefaultConfigConverter;

import java.lang.reflect.Type;

/**
 * 配置应用服务
 *
 * @author jay.wu
 */
public class SimpleConfigAppService {

    private final SimpleConfigConverter simpleConfigConverter;

    public SimpleConfigAppService(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigConverter = new DefaultConfigConverter(simpleConfigRepository);
    }

    /**
     * 将多个配置项转换为指定的配置类
     * <p>
     * 支持动态下发配置后实时生效
     *
     * @param toType     目标配置类型
     * @param configType 配置项类型或配置项前缀
     * @param <T>        目标配置类型
     * @return 目标配置类
     * @see org.team4u.config.domain.repository.CacheableSimpleConfigRepository
     */
    public <T> T to(Class<T> toType, String configType) {
        return simpleConfigConverter.to(toType, configType);
    }

    /**
     * 根据特定的配置项转换为指定的配置类
     * <p>
     * 该方法将缓存相同值的配置项
     *
     * @param toType     目标配置类型
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @param <T>        目标配置类型
     * @return 目标配置类
     */
    public <T> T to(Type toType, String configType, String configKey) {
        return simpleConfigConverter.to(toType, configType, configKey);
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
    public <T> T to(Type toType, String configType, String configKey, boolean isCacheResult) {
        return simpleConfigConverter.to(toType, configType, configKey, isCacheResult);
    }

    /**
     * 获取指定配置项对应的值
     *
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @return 配置值
     */
    public String value(String configType, String configKey) {
        return simpleConfigConverter.value(configType, configKey);
    }

    /**
     * 获取所有配置项
     *
     * @return 配置项集合
     */
    public SimpleConfigs allConfigs() {
        return simpleConfigConverter.allConfigs();
    }
}