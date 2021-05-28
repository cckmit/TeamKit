package org.team4u.rl.application;


import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.persistence.JsonRateLimitConfigRepository;

public abstract class AbstractRateLimiterAppServiceTest {

    private final RateLimiterAppService service = rateLimiterAppService();

    @Test
    public void tryAcquire() {
        Assert.assertTrue(service.tryAcquire("test", "1"));
        Assert.assertFalse(service.tryAcquire("test", "1"));

        ThreadUtil.safeSleep(201);
        Assert.assertTrue(service.tryAcquire("test", "1"));
    }

    @Test
    public void countAcquired() {
        // 无请求，次数=0
        Assert.assertEquals(0, service.countAcquired("test", "1"));

        // 请求1次
        service.tryAcquire("test", "1");
        Assert.assertEquals(1, service.countAcquired("test", "1"));

        // 超时后重置，次数=0
        ThreadUtil.safeSleep(201);
        Assert.assertEquals(0, service.countAcquired("test", "1"));
    }

    @Test
    public void canAcquire() {
        // 无请求
        Assert.assertTrue(service.canAcquire("test", "1"));

        // 请求1次
        service.tryAcquire("test", "1");
        Assert.assertFalse(service.canAcquire("test", "1"));

        // 超时后重置
        ThreadUtil.safeSleep(201);
        Assert.assertTrue(service.canAcquire("test", "1"));
    }

    protected abstract RateLimiterFactory newRateLimiterFactory();

    private RateLimiterAppService rateLimiterAppService() {
        return new RateLimiterAppService(
                new RateLimiterAppService.LimitersRefresher.Config()
                        .setConfigId("config/test")
                        .setRefreshConfigIntervalMillis(50),
                newRateLimiterFactory(),
                rateLimitConfigRepository()
        );
    }

    protected RateLimitConfigRepository rateLimitConfigRepository() {
        return new JsonRateLimitConfigRepository(new LocalJsonConfigService());
    }
}