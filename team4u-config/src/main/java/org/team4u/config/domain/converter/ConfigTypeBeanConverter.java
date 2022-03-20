package org.team4u.config.domain.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 基于配置类型的配置对象转换器
 *
 * @author jay.wu
 */
public class ConfigTypeBeanConverter {

    private final ConfigValueBeanConverter configValueBeanConverter = new ConfigValueBeanConverter();

    /**
     * 将配置项转换为目标对象
     */
    public <T> T convert(SimpleConfigs simpleConfigs, String configType, Class<T> toType) {
        return BeanUtil.fillBean(
                ReflectUtil.newInstanceIfPossible(toType),
                new ConfigBeanValueProvider(simpleConfigs, configType),
                CopyOptions.create()
        );
    }

    public class ConfigBeanValueProvider implements ValueProvider<String> {

        private final SimpleConfigs simpleConfigs;

        private final String configType;

        public ConfigBeanValueProvider(SimpleConfigs simpleConfigs, String configType) {
            this.simpleConfigs = simpleConfigs;
            this.configType = configType;
        }

        @Override
        public Object value(String key, Type valueType) {
            return configValueBeanConverter.convert(
                    simpleConfigs,
                    valueType,
                    configType,
                    key,
                    false
            );
        }

        @Override
        public boolean containsKey(String key) {
            return simpleConfigs.filter(configType, key) != null;
        }
    }
}