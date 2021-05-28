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
     * 统计尝试访问次数
     *
     * @param type 类型
     * @param key  键值
     * @return 成功访问次数
     */
    long countAcquired(String type, String key);

    /**
     * 是否可以访问
     *
     * @param type 类型
     * @param key  键值
     * @return 是否可以访问
     */
    boolean canAcquire(String type, String key);
}
