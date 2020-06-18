package org.team4u.ddd.process.strategy;

/**
 * 重试策略资源库
 *
 * @author jay.wu
 */
public interface RetryStrategyRepository {

    /**
     * 默认策略标识
     */
    String DEFAULT_STRATEGY_ID = "default";

    /**
     * 获取重试策略
     *
     * @param id 策略标识
     * @return 重试策略
     */
    RetryStrategy strategyOf(String id);
}