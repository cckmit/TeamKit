package org.team4u.ddd.process.strategy;

import org.team4u.ddd.process.MockRetryStrategy;


public class FakeRetryStrategyRepository implements RetryStrategyRepository {

    @Override
    public RetryStrategy strategyOf(String id) {
        return new MockRetryStrategy();
    }
}
