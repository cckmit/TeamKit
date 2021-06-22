package org.team4u.config.infrastructure.converter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONUtil;
import net.bytebuddy.matcher.ElementMatchers;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.aop.SimpleAop;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class ProxySimpleConfigConverter implements SimpleConfigConverter {

    private final SimpleConfigRepository simpleConfigRepository;
    private final static String METHOD_NAME_MATCHES = "is|get(\\w+)";

    public ProxySimpleConfigConverter(SimpleConfigRepository simpleConfigRepository) {
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public <T> T to(Class<T> toType, String configType) {
        return SimpleAop.proxyOf(
                toType,
                ElementMatchers.nameMatches(METHOD_NAME_MATCHES),
                new ValueMethodInterceptor(toType, configType)
        );
    }

    @Override
    public <T> T to(Type toType, String configType, String configKey) {
        String value = value(configType, configKey);
        if (value == null) {
            return null;
        }

        if (ClassUtil.isSimpleTypeOrArray(TypeUtil.getClass(toType))) {
            return Convert.convert(toType, value);
        } else {
            return JSONUtil.toBean(value, toType, false);
        }
    }

    @Override
    public String value(String configType, String configKey) {
        SimpleConfig config = filterConfig(allConfigs(), configType, configKey);
        if (config == null) {
            return null;
        }

        return config.getConfigValue();
    }

    private List<SimpleConfig> allConfigs() {
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

        private Object proxy = null;
        private List<SimpleConfig> oldSimpleConfigs;

        private ValueMethodInterceptor(Class<?> targetType, String configType) {
            this.targetType = targetType;
            this.configType = configType;
            oldSimpleConfigs = allConfigs();
        }

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) {
            return ReflectUtil.invoke(beanOf(targetType), method, parameters);
        }

        private boolean isNoChanged() {
            return oldSimpleConfigs.equals(allConfigs());
        }

        private Object beanOf(Class<?> classType) {
            if (proxy != null && isNoChanged()) {
                return proxy;
            }

            oldSimpleConfigs = allConfigs();

            proxy = BeanUtil.fillBean(ReflectUtil.newInstanceIfPossible(classType),
                    new ValueProvider<String>() {
                        @Override
                        public Object value(String key, Type valueType) {
                            return toValue(key, valueType);
                        }

                        @Override
                        public boolean containsKey(String key) {
                            return filterConfig(allConfigs(), configType, key) != null;
                        }
                    },
                    CopyOptions.create());

            return proxy;
        }

        private <T> T toValue(Class<?> toTypeClass, Type toValueType, String valueString) {
            // 简单类型直接注入
            if (ClassUtil.isSimpleTypeOrArray(toTypeClass) ||
                    Collection.class.isAssignableFrom(toTypeClass)) {
                return Convert.convert(toValueType, valueString);
            }

            // 复杂类型只支持json格式
            return JSONUtil.toBean(valueString, toValueType, false);
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