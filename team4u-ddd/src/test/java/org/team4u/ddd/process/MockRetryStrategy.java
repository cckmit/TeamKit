package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.RetryStrategy;
import org.team4u.ddd.TestUtil;

public class MockRetryStrategy implements RetryStrategy {

    public static final MockRetryStrategy INSTANCE = new MockRetryStrategy();

    @Override
    public String strategyId() {
        return TestUtil.TEST_ID;
    }

    @Override
    public int nextIntervalSec(int retryCount) {
        return 1;
    }

    @Override
    public int maxRetriesPermitted() {
        return 0;
    }
}
