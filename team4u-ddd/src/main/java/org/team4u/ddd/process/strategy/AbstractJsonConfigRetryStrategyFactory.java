package org.team4u.ddd.process.strategy;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;

/**
 * 基于JSON配置的重试策略抽象工厂
 *
 * @param <R> 重试策略类型
 * @param <C> 重试策略配置类型
 * @author jay.wu
 */
public abstract class AbstractJsonConfigRetryStrategyFactory<
        R extends RetryStrategy,
        C extends AbstractRetryStrategy.AbstractConfig<C>>
        implements StringConfigRetryStrategyFactory<R, C> {

    @Override
    public R create(String jsonConfig) {
        C config = JSONUtil.toBean(
                jsonConfig,
                configType()
        );

        return create(config);
    }

    @Override
    public R create(C config) {
        return ReflectUtil.newInstance(strategyType(), config);
    }

    @SuppressWarnings("unchecked")
    protected Class<R> strategyType() {
        return (Class<R>) ClassUtil.getTypeArgument(this.getClass(), 0);
    }

    @SuppressWarnings("unchecked")
    protected Class<C> configType() {
        return (Class<C>) ClassUtil.getTypeArgument(this.getClass(), 1);
    }
}