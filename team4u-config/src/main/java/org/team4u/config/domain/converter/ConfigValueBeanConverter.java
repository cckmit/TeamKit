package org.team4u.config.domain.converter;

import org.team4u.base.serializer.JsonOrSimpleValueSerializer;
import org.team4u.base.serializer.Serializer;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 基于配置值的配置对象转换器
 *
 * @author jay.wu
 */
public class ConfigValueBeanConverter {

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
    public <T> T convert(SimpleConfigs configs, Type toType, String configType, String configKey, boolean isCacheResult) {
        String value = configs.value(configType, configKey);
        if (value == null) {
            return null;
        }

        return serializer(isCacheResult).deserialize(value, toType);
    }

    protected Serializer serializer(boolean isCacheResult) {
        if (isCacheResult) {
            return JsonOrSimpleValueSerializer.cache();
        }

        return JsonOrSimpleValueSerializer.noCache();
    }
}