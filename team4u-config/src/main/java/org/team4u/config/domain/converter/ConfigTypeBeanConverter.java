package org.team4u.config.domain.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import org.team4u.base.serializer.HutoolJsonSerializer;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 基于配置类型的配置对象转换器
 *
 * @author jay.wu
 */
public class ConfigTypeBeanConverter {

    /**
     * 将配置项转换为目标对象
     */
    public Object convert(SimpleConfigs simpleConfigs, String configType, Class<?> toType) {
        return BeanUtil.fillBean(
                ReflectUtil.newInstanceIfPossible(toType),
                new ValueProvider<String>() {
                    @Override
                    public Object value(String key, Type valueType) {
                        return toValue(simpleConfigs.filter(configType, key), valueType);
                    }

                    @Override
                    public boolean containsKey(String key) {
                        return simpleConfigs.filter(configType, key) != null;
                    }
                },
                CopyOptions.create()
        );
    }

    private <T> T toValue(Class<?> toTypeClass, Type toValueType, String valueString) {
        // 简单类型直接注入
        if (ClassUtil.isSimpleTypeOrArray(toTypeClass) ||
                Collection.class.isAssignableFrom(toTypeClass)) {
            try {
                return Convert.convert(toValueType, valueString);
            } catch (Exception e) {
                // Ignore error
            }
        }

        // 复杂类型只支持json格式
        return HutoolJsonSerializer.instance().deserialize(valueString, toValueType);
    }

    private Object toValue(SimpleConfig simpleConfig, Type valueType) {
        if (simpleConfig == null || simpleConfig.getConfigValue() == null) {
            return null;
        }

        return toValue(TypeUtil.getClass(valueType), valueType, simpleConfig.getConfigValue());
    }
}