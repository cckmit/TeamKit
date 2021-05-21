package org.team4u.rl.infrastructure.service;


import org.team4u.rl.domain.RateLimiterService;

public class InMemoryRateLimiterServiceTest extends AbstractRateLimiterServiceTest {

    private final InMemoryRateLimiterService service = new InMemoryRateLimiterService(
            new AbstractRateLimiterService.Config().setConfigId("config/test"),
            rateLimitConfigRepository()
    );

    @Override
    protected RateLimiterService newRateLimiterService() {
        return service;
    }
}