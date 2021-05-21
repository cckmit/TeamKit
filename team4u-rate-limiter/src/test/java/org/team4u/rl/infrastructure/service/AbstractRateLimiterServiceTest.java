package org.team4u.rl.infrastructure.service;


import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiterService;
import org.team4u.rl.infrastructure.persistence.JsonRateLimitConfigRepository;

public abstract class AbstractRateLimiterServiceTest {

    @Test
    public void tryAcquire() {
        RateLimiterService service = newRateLimiterService();
        ThreadUtil.safeSleep(500);

        Assert.assertTrue(service.tryAcquire("test", "1"));
        Assert.assertFalse(service.tryAcquire("test", "1"));

        ThreadUtil.safeSleep(500);
        Assert.assertTrue(service.tryAcquire("test", "1"));
    }

    protected abstract RateLimiterService newRateLimiterService();

    protected RateLimitConfigRepository rateLimitConfigRepository() {
        return new JsonRateLimitConfigRepository(new LocalJsonConfigService());
    }
}