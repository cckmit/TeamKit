package org.team4u.rl.application;

import org.team4u.rl.domain.RateLimiterService;

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

    public boolean tryAcquire(String type, String key) {
        // 任意一个限流管理器不允许访问，则返回false
        for (RateLimiterService rateLimiter : limiterServices) {
            if (!rateLimiter.tryAcquire(type, key)) {
                return false;
            }
        }

        return true;
    }
}