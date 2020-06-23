package org.team4u.ddd.process.strategy;


import org.team4u.core.lang.IdObject;

/**
 * 重试策略构建工厂
 *
 * @author jay.wu
 */
public interface RetryStrategyFactory<
        R extends RetryStrategy,
        C extends AbstractRetryStrategy.AbstractConfig<C>> extends IdObject<String> {

    /**
     * 创建重试策略
     *
     * @param config 配置类
     * @return 重试策略
     */
    R create(C config);
}