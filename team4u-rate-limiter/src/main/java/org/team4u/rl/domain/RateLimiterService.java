package org.team4u.rl.domain;

public interface RateLimiterService {

    /**
     * 根据类型尝试是否允许访问
     *
     * @param type 类型
     * @param key  键值
     * @return true为可以访问，false为拒绝访问
     */
    boolean tryAcquire(String type, String key);


    /**
     * 获取尝试访问次数
     *
     * @param type 类型
     * @param key  键值
     * @return 成功访问次数
     */
    long tryAcquiredCount(String type, String key);
}
