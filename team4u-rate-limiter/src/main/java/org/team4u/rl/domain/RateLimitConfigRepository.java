package org.team4u.rl.domain;

/**
 * 限流配置资源库
 *
 * @author jay.wu
 */
public interface RateLimitConfigRepository {

    /**
     * 获取配置
     *
     * @param configId 配置标识
     * @return 配置
     */
    RateLimiterConfig configOfId(String configId);
}