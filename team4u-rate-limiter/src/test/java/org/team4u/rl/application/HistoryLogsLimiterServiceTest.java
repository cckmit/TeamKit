package org.team4u.rl.application;

import org.junit.Before;
import org.team4u.rl.domain.RateLimiterFactory;
import org.team4u.rl.infrastructure.limiter.HistoryLogsLimiter;

import java.util.ArrayList;

public class HistoryLogsLimiterServiceTest extends AbstractRateLimiterAppServiceTest {

    @Before
    public void setUp() {
        HistoryLogsLimiter.setDatasource(new ArrayList<>());
    }

    @Override
    protected RateLimiterFactory newRateLimiterFactory() {
        return new HistoryLogsLimiter.Factory();
    }
}