package org.team4u.rl.infrastructure.limiter;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于本地内存的简单计数限流器
 *
 * @author jay.wu
 */
public class InMemoryCountRateLimiter implements RateLimiter {

    private final RateLimiterConfig.Rule config;
    private final Cache<String, AtomicLong> counters;

    public InMemoryCountRateLimiter(RateLimiterConfig.Rule config) {
        this.config = config;
        this.counters = new TimedCache<>(config.getExpirationMillis());
    }

    @Override
    public synchronized boolean tryAcquire(String key) {
        AtomicLong counter = counters.get(key);

        if (counter == null) {
            counter = new AtomicLong();
            counters.put(key, counter);
        }

        return counter.incrementAndGet() <= config.getThreshold();
    }

    @Override
    public long tryAcquiredCount(String key) {
        AtomicLong counter = counters.get(key);

        if (counter == null) {
            return 0;
        }
        return counter.get();
    }
}