package org.team4u.rl.domain;

/**
 * @author jay.wu
 */
public interface RateLimiter {

    /**
     * 尝试访问
     *
     * @param key 关键key
     * @return true为可以访问，false为拒绝访问
     */
    boolean tryAcquire(String key);

    /**
     * 统计尝试访问次数
     *
     * @param key 关键key
     * @return 成功访问次数
     */
    long countAcquired(String key);

    /**
     * 是否可以访问
     *
     * @param key 键值
     * @return 是否可以访问
     */
    boolean canAcquire(String key);
}