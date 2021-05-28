package org.team4u.rl.application;


import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.InMemoryCountRateLimiter;

public class InMemoryRateLimiterServiceTest extends AbstractRateLimiterAppServiceTest {

    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new InMemoryCountRateLimiter.Factory();
    }
}