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

    public boolean tryAcquire(String type, String key) {
        return Arrays.stream(limiterServices)
                .allMatch(it -> it.tryAcquire(type, key));
    }

    public long tryAcquiredCount(String type, String key) {
        return Arrays.stream(limiterServices)
                .map(it -> it.tryAcquiredCount(type, key))
                .max(Long::compareTo)
                .orElse(0L);
    }

    public boolean canAcquire(String type, String key) {
        return Arrays.stream(limiterServices)
                .allMatch(it -> it.canAcquire(type, key));
    }
}