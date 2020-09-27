package org.team4u.ddd.process.strategy;

/**
 * 重试策略
 *
 * @author jay.wu
 */
public interface RetryStrategy {

    /**
     * 获取策略标识
     *
     * @return 策略标识
     */
    String strategyId();

    /**
     * 下次重试的间隔时间
     *
     * @param retryCount 当前重试次数
     * @return 间隔时间（秒）
     */
    int nextIntervalSec(int retryCount);

    /**
     * 获取最大允许重试次数
     * <p>
     * -1代表永久重试
     *
     * @return 最大允许重试次数
     */
    int maxRetriesPermitted();
}