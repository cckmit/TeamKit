package org.team4u.rl.domain;

/**
 * @author Jay Wu
 */
public interface RateLimiterManager {

    /**
     * 根据类型尝试是否允许访问
     *
     * @return true为可以访问，false为拒绝访问
     */
    boolean tryAcquire(String type, String key);
}