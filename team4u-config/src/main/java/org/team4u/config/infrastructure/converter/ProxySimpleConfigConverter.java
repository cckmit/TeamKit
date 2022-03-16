package org.team4u.config.infrastructure.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.log.Log;
import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.aop.SimpleAop;
import org.team4u.base.log.LogMessage;
import org.team4u.base.serializer.*;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * 动态配置转换器
 * <p>
 * 当配置项动态下发后，配置类能够实时获取最新的配置值
 *
 * @author jay.wu
 */
public class ProxySimpleConfigConverter implements SimpleConfigConverter {

    private final Log log = Log.get();

    private final SimpleConfigRepository simpleConfigRepository;

    public ProxySimpleConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        return SimpleAop.proxyOf(
                toType,
                ElementMatchers.isGetter(),
                new ValueMethodInterceptor(toType, configType)
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
        SimpleConfig config = filterConfig(allConfigs(), configType, configKey);
        if (config == null) {
            return null;
        }

        return config.getConfigValue();
    }

    @Override
    public List<SimpleConfig> allConfigs() {
        return simpleConfigRepository.allConfigs();
    }

    private SimpleConfig filterConfig(List<SimpleConfig> simpleConfigs, String configType, String configKey) {
        return simpleConfigs.stream()
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigType(), configType))
                .filter(it -> StrUtil.equals(it.getConfigId().getConfigKey(), configKey))
                .filter(SimpleConfig::getEnabled)
                .findFirst()
                .orElse(null);
    }

    private class ValueMethodInterceptor implements MethodInterceptor {

        private final String configType;
        private final Class<?> targetType;

        private List<SimpleConfig> currentSimpleConfigs = Collections.emptyList();

        private ValueMethodInterceptor(Class<?> targetType, String configType) {
            this.targetType = targetType;
            this.configType = configType;
        }

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Exception {
            // 配置项没有变化，直接返回
            if (!isConfigChange()) {
                return superMethod.call();
            }

            synchronized (this) {
                if (isConfigChange()) {
                    // 获取最新配置项
                    List<SimpleConfig> latestConfigs = latestConfigs();
                    // 将最新的代理对象字段值赋值到当前对象
                    BeanUtil.copyProperties(buildProxy(latestConfigs, targetType), instance);
                    // 刷新配置项
                    currentSimpleConfigs = latestConfigs;
                }
            }

            return superMethod.call();
        }

        /**
         * 判断配置项是否存在变动
         */
        private boolean isConfigChange() {
            return !currentSimpleConfigs.equals(allConfigs());
        }

        private List<SimpleConfig> latestConfigs() {
            return allConfigs()
                    .stream()
                    .map(it -> new SimpleConfig(
                            it.getConfigId(),
                            it.getConfigValue(),
                            it.getDescription(),
                            it.getSequenceNo(),
                            it.getEnabled(),
                            it.getCreatedBy(),
                            it.getCreateTime()
                    ))
                    .collect(Collectors.toList());
        }

        private Object buildProxy(List<SimpleConfig> configs, Class<?> classType) {
            Object proxy = BeanUtil.fillBean(ReflectUtil.newInstanceIfPossible(classType),
                    new ValueProvider<String>() {
                        @Override
                        public Object value(String key, Type valueType) {
                            return toValue(key, valueType);
                        }

                        @Override
                        public boolean containsKey(String key) {
                            return filterConfig(configs, configType, key) != null;
                        }
                    },
                    CopyOptions.create());


            log.info(LogMessage.create(ProxySimpleConfigConverter.class.getSimpleName(), "build")
                    .success()
                    .append("type", classType.getName())
                    .toString());
            return proxy;
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

        private Object toValue(String key, Type valueType) {
            SimpleConfig simpleConfig = filterConfig(allConfigs(), configType, key);

            if (simpleConfig == null || simpleConfig.getConfigValue() == null) {
                return null;
            }

            return toValue(TypeUtil.getClass(valueType), valueType, simpleConfig.getConfigValue());
        }
    }
}