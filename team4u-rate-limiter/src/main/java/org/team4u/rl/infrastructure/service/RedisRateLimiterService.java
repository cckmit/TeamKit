package org.team4u.rl.infrastructure.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.infrastructure.limiter.RedisCountRateLimiter;

public class RedisRateLimiterService extends AbstractRateLimiterService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRateLimiterService(Config config,
                                   RateLimitConfigRepository configRepository,
                                   RedisTemplate<String, String> redisTemplate) {
        super(config, configRepository);
        this.redisTemplate = redisTemplate;

        start();
    }

    @Override
    protected RateLimiter createRateLimiter(RateLimiterConfig.Rule rule) {
        return new RedisCountRateLimiter(rule, redisTemplate);
    }
}