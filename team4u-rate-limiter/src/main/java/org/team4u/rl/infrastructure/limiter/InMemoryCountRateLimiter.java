package org.team4u.rl.infrastructure.limiter;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.TimedCache;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于本地内存的简单计数限流器
 *
 * @author Jay Wu
 */
public class InMemoryCountRateLimiter implements RateLimiter {

    private final RateLimiterConfig.Rule config;
    private final Cache<String, AtomicInteger> counters;

    public InMemoryCountRateLimiter(RateLimiterConfig.Rule config) {
        this.config = config;
        this.counters = new TimedCache<>(config.getExpirationMillis());
    }

    @Override
    public synchronized boolean tryAcquire(String key) {
        AtomicInteger counter = counters.get(key);

        if (counter == null) {
            counter = new AtomicInteger();
            counters.put(key, counter);
        }

        // 超出阀值不在累加统计
        if (counter.get() > config.getThreshold()) {
            return false;
        }

        return counter.incrementAndGet() <= config.getThreshold();
    }
}