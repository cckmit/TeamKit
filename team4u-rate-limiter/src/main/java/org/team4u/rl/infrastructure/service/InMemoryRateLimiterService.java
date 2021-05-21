package org.team4u.rl.infrastructure.service;

import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.infrastructure.limiter.InMemoryCountRateLimiter;

public class InMemoryRateLimiterService extends AbstractRateLimiterService {

    public InMemoryRateLimiterService(Config config, RateLimitConfigRepository configRepository) {
        super(config, configRepository);

        start();
    }

    @Override
    protected RateLimiter createRateLimiter(RateLimiterConfig.Rule rule) {
        return new InMemoryCountRateLimiter(rule);
    }
}
