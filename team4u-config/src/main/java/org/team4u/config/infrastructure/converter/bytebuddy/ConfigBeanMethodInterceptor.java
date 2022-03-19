package org.team4u.config.infrastructure.converter.bytebuddy;

import lombok.Getter;
import org.team4u.base.aop.MethodInterceptor;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;
import org.team4u.config.domain.converter.ConfigBeanRefresher;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 配置对象拦截器
 *
 * @author jay.wu
 */
@Getter
public class ConfigBeanMethodInterceptor implements MethodInterceptor {

    private final ConfigBeanRefresher configBeanRefresher;
    private final SimpleConfigRepository simpleConfigRepository;

    public ConfigBeanMethodInterceptor(ConfigBeanRefresher configBeanRefresher,
                                       SimpleConfigRepository simpleConfigRepository) {
        this.configBeanRefresher = configBeanRefresher;
        this.simpleConfigRepository = simpleConfigRepository;
    }

    @Override
    public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Exception {
        SimpleConfigs latestConfigs = simpleConfigRepository.allConfigs();

        // 配置项没有变化，直接返回
        if (configBeanRefresher.isNotNeedToRefresh(latestConfigs)) {
            return superMethod.call();
        }

        // 刷新配置对象
        configBeanRefresher.refresh(latestConfigs);
        return superMethod.call();
    }
}