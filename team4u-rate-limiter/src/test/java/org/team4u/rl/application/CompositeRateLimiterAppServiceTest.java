package org.team4u.rl.application;

import org.junit.Before;
import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.CompositeRateLimiter;
import org.team4u.rl.infrastructure.limiter.InMemoryCountRateLimiter;
import org.team4u.rl.infrastructure.limiter.RedisCountRateLimiter;

public class CompositeRateLimiterAppServiceTest extends AbstractRateLimiterAppServiceTest {

    @Before
    public void setUp() {
        TestUtil.createRedisTemplate().delete("1");
    }

    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new CompositeRateLimiter.Factory(
                new InMemoryCountRateLimiter.Factory(),
                new RedisCountRateLimiter.Factory(TestUtil.createRedisTemplate())
        );
    }
}