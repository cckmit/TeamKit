package org.team4u.config.infrastructure.converter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
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
                ElementMatchers.any(),
                new ValueMethodInterceptor(configType)
        );
    }

    @Override
    public <T> T to(Class<T> toType, String configType, String configKey) {
        SimpleConfig config = filterConfig(allConfigs(), configType, configKey);
        if (config == null || config.getConfigValue() == null) {
            return null;
        }

        if (ClassUtil.isSimpleTypeOrArray(toType)) {
            return Convert.convert(toType, config.getConfigValue());
        } else {
            return JSONUtil.toBean(config.getConfigValue(), toType);
        }
    }

    @Override
    public String to(String configType, String configKey) {
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

        private ValueMethodInterceptor(String configType) {
            this.configType = configType;
        }

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) {
            String configKey = StrUtil.lowerFirst(ReUtil.get(METHOD_NAME_MATCHES, method.getName(), 1));
            if (configKey == null) {
                return null;
            }

            SimpleConfig simpleConfig = filterConfig(allConfigs(), configType, configKey);
            if (simpleConfig == null || simpleConfig.getConfigValue() == null) {
                return null;
            }

            return toValue(method.getReturnType(), method.getGenericReturnType(), simpleConfig.getConfigValue());
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
    }
}
