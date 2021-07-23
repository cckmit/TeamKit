package org.team4u.rl.domain;

/**
 * 限流器构建工厂
 *
 * @author jay.wu
 */
public interface RateLimiterFactory {

    /**
     * 创建限流器
     *
     * @param config 规则
     * @return 限流器
     */
    RateLimiter create(RateLimiterConfig config);
}