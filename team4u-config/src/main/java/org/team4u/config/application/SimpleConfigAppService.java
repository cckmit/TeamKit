package org.team4u.config.application;

import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 配置应用服务
 *
 * @author jay.wu
 */
public class SimpleConfigAppService {

    private final SimpleConfigConverter simpleConfigConverter;

    public SimpleConfigAppService(SimpleConfigConverter simpleConfigConverter) {
        this.simpleConfigConverter = simpleConfigConverter;
    }

    /**
     * 将多个配置项转换为指定的配置类
     *
     * @param toType     目标配置类型
     * @param configType 配置项类型或配置项前缀
     * @param <T>        目标配置类型
     * @return 目标配置类
     */
    public <T> T to(Class<T> toType, String configType) {
        return simpleConfigConverter.to(configType, toType);
    }

    /**
     * 根据特定的配置项转换为指定的配置类
     *
     * @param toType     目标配置类型
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @param <T>        目标配置类型
     * @return 目标配置类
     */
    public <T> T to(Type toType, String configType, String configKey) {
        return simpleConfigConverter.to(configType, configKey, toType);
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