package org.team4u.rl.application;

import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.RedisCountRateLimiter;

public class RedisRateLimiterServiceTest extends AbstractRateLimiterAppServiceTest {
    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new RedisCountRateLimiter.Factory(TestUtil.createRedisTemplate());
    }
}