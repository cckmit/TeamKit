package org.team4u.config.domain.converter.value;

import org.team4u.base.serializer.SmartSerializer;
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
     * @param <T>        目标配置类型
     * @param configType 配置类型或配置前缀
     * @param configKey  配置项key
     * @param toType     目标配置类型
     * @return 目标配置类
     */
    public <T> T convert(SimpleConfigs configs, String configType, String configKey, Type toType) {
        return convert(configs.value(configType, configKey), toType);
    }

    public <T> T convert(String value, Type toType) {
        if (value == null) {
            return null;
        }

        return SmartSerializer.getInstance().deserialize(value, toType);
    }
}