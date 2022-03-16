package org.team4u.config.infrastructure.converter.simple;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.base.log.LogMessage;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigs;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 配置对象拦截器
 *
 * @author jay.wu
 */
public class ConfigBeanMethodInterceptor implements MethodInterceptor {

    private final Log log = Log.get();

    private final String configType;
    private final Class<?> targetType;
    private SimpleConfigs currentConfigs = new SimpleConfigs();

    private final ConfigBeanConverter beanConverter = new ConfigBeanConverter();
    private final SimpleConfigConverter simpleConfigConverter;

    public ConfigBeanMethodInterceptor(Class<?> targetType,
                                       String configType,
                                       SimpleConfigConverter simpleConfigConverter) {
        this.targetType = targetType;
        this.configType = configType;
        this.simpleConfigConverter = simpleConfigConverter;
    }

    @Override
    public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Exception {
        SimpleConfigs latestConfigs = latestConfigs();

        // 配置项没有变化，直接返回
        if (sameAs(latestConfigs)) {
            return superMethod.call();
        }

        synchronized (this) {
            refresh(latestConfigs, instance);
        }

        return superMethod.call();
    }

    private SimpleConfigs latestConfigs() {
        return simpleConfigConverter.allConfigs().filter(configType);
    }

    private void refresh(SimpleConfigs latestConfigs, Object instance) {
        LogMessage lm = logMessageOf(latestConfigs);

        if (sameAs(latestConfigs)) {
            log.info(lm.fail("Configs has not changed").toString());
            return;
        }

        // 刷新当前配置对象
        refreshInstance(latestConfigs, instance);
        // 刷新当前配置项
        refreshConfigs(latestConfigs);

        log.info(lm.success().toString());
    }

    private LogMessage logMessageOf(SimpleConfigs configs) {
        return LogMessage.create(targetType.getSimpleName(), "refresh")
                .append("configId", Optional.ofNullable(CollUtil.getFirst(configs.getValue()))
                        .map(it -> it.getConfigId().getConfigType())
                        .orElse(null));
    }

    private void refreshInstance(SimpleConfigs latestConfigs, Object instance) {
        Object newInstance = beanConverter.convert(latestConfigs, configType, targetType);
        // 将最新的代理对象字段值赋值到当前对象
        BeanUtil.copyProperties(newInstance, instance);
    }

    private void refreshConfigs(SimpleConfigs latestConfigs) {
        currentConfigs = latestConfigs.copy();
    }

    /**
     * 判断配置项是否未变动
     */
    private boolean sameAs(SimpleConfigs latestConfigs) {
        return currentConfigs.equals(latestConfigs);
    }
}