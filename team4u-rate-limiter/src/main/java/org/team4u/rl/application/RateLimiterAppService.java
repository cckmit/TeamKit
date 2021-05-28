package org.team4u.rl.application;

import org.team4u.rl.domain.RateLimiterService;

import java.util.Arrays;

/**
 * 限流应用服务
 *
 * @author jay.wu
 */
public class RateLimiterAppService {

    private final RateLimiterService[] limiterServices;

    public RateLimiterAppService(RateLimiterService... limiterServices) {
        this.limiterServices = limiterServices;
    }

    /**
     * 根据类型尝试是否允许访问
     *
     * @param type 类型
     * @param key  键值
     * @return true为可以访问，false为拒绝访问
     */
    public boolean tryAcquire(String type, String key) {
        return Arrays.stream(limiterServices)
                .allMatch(it -> it.tryAcquire(type, key));
    }

    /**
     * 获取尝试访问次数
     *
     * @param type 类型
     * @param key  键值
     * @return 成功访问次数
     */
    public long tryAcquiredCount(String type, String key) {
        return Arrays.stream(limiterServices)
                .map(it -> it.tryAcquiredCount(type, key))
                .max(Long::compareTo)
                .orElse(0L);
    }

    /**
     * 是否可以访问
     *
     * @param type 类型
     * @param key  键值
     * @return 是否可以访问
     */
    public boolean canAcquire(String type, String key) {
        return Arrays.stream(limiterServices)
                .allMatch(it -> it.canAcquire(type, key));
    }
}