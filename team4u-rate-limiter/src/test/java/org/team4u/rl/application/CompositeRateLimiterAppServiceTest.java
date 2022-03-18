package org.team4u.rl.application;

import org.junit.Before;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.CompositeRateLimiter;
import org.team4u.rl.infrastructure.limiter.InMemoryCountRateLimiter;
import org.team4u.rl.infrastructure.limiter.RedisCountRateLimiter;

public class CompositeRateLimiterAppServiceTest extends AbstractRateLimiterAppServiceTest {

    private final StringRedisTemplate template = TestUtil.createRedisTemplate();

    @Before
    public void setUp() {
        template.delete("1");
    }

    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new CompositeRateLimiter.Factory(
                new InMemoryCountRateLimiter.Factory(),
                new RedisCountRateLimiter.Factory(template)
        );
    }
}