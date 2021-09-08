package org.team4u.rl.application;

import org.junit.Before;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.RedisCountRateLimiter;

public class RedisRateLimiterServiceTest extends AbstractRateLimiterAppServiceTest {

    private final static StringRedisTemplate redisTemplate = TestUtil.createRedisTemplate();

    @Before
    public void setUp() {
        redisTemplate.delete("1");
    }

    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new RedisCountRateLimiter.Factory(redisTemplate);
    }
}