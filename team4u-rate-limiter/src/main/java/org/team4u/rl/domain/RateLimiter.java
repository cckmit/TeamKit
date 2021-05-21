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
}