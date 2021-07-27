package org.team4u.rl.application;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.config.PropConfigService;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.infrastructure.limiter.InMemoryCountRateLimiter;
import org.team4u.rl.infrastructure.persistence.JsonRateLimitConfigRepository;

public class RateLimiterAppServiceTest {

    private final PropConfigService propConfigService = new PropConfigService();
    private final RateLimiterAppService appService = rateLimiterAppService();

    @Test
    public void rateLimiterOf() {
        checkConfig(1);
        checkConfig(2);
        checkConfig(2);
    }

    private void checkConfig(int threshold) {
        propConfigService.getProperties().setProperty("test", "{\n" +
                "  \"expirationMillis\": 200,\n" +
                "  \"threshold\": " + threshold + "\n" +
                "}");
        RateLimiter limiter = appService.rateLimiterOf("test");
        Assert.assertEquals(threshold, limiter.config().getThreshold());
    }

    private RateLimiterAppService rateLimiterAppService() {
        return new RateLimiterAppService(
                new InMemoryCountRateLimiter.Factory(),
                rateLimitConfigRepository()
        );
    }

    protected RateLimitConfigRepository rateLimitConfigRepository() {
        return new JsonRateLimitConfigRepository(propConfigService);
    }
}