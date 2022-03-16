package org.team4u.config.infrastructure.converter.simple;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.SimpleAop;
import org.team4u.base.serializer.*;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Type;

/**
 * 默认配置转换器
 * <p>
 * 当配置项动态下发后，配置类能够实时获取最新的配置值
 *
 * @author jay.wu
 */
public class DefaultConfigConverter implements SimpleConfigConverter {


    private final SimpleConfigRepository simpleConfigRepository;

    public DefaultConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        return SimpleAop.proxyOf(
                toType,
                ElementMatchers.isGetter(),
                new ConfigBeanMethodInterceptor(toType, configType, this)
        );
    }

    @Override
    public <T> T to(Type toType, String configType, String configKey, boolean isCacheResult) {
        String value = value(configType, configKey);
        if (value == null) {
            return null;
        }

        // 简单类型直接转换
        if (ClassUtil.isSimpleTypeOrArray(TypeUtil.getClass(toType))) {
            return simpleSerializer(isCacheResult).deserialize(value, toType);
        }

        // 复杂类型通过JSON反序列化
        return jsonSerializer(isCacheResult).deserialize(value, toType);
    }

    protected Serializer simpleSerializer(boolean isCacheResult) {
        // 缓存结果对象，仅value不同时进行反序列化
        if (isCacheResult) {
            return SimpleValueCacheSerializer.instance();
        }

        // 不缓存结果对象，每次进行反序列化
        return SimpleValueSerializer.instance();
    }

    protected Serializer jsonSerializer(boolean isCacheResult) {
        // 缓存结果对象，仅value不同时进行反序列化
        if (isCacheResult) {
            return HutoolJsonCacheSerializer.instance();
        }

        // 不缓存结果对象，每次进行反序列化
        return HutoolJsonSerializer.instance();
    }

    @Override
    public String value(String configType, String configKey) {
        SimpleConfig config = allConfigs().filter(configType, configKey);
        if (config == null) {
            return null;
        }

        return config.getConfigValue();
    }

    @Override
    public SimpleConfigs allConfigs() {
        return simpleConfigRepository.allConfigs();
    }
}