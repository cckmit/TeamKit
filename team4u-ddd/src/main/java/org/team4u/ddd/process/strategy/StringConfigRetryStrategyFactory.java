package org.team4u.ddd.process.strategy;

/**
 * 基于文本配置的重试策略构建工厂
 *
 * @author jay.wu
 */
public interface StringConfigRetryStrategyFactory<
        R extends RetryStrategy,
        C extends AbstractRetryStrategy.AbstractConfig<C>> extends RetryStrategyFactory<R, C> {

    /**
     * 创建重试策略
     *
     * @param config 配置内容
     * @return 重试策略
     */
    R create(String config);
}