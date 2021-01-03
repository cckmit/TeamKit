package org.team4u.ddd.process.strategy;

import org.springframework.stereotype.Service;
import org.team4u.ddd.process.MockRetryStrategy;

@Service
public class FakeRetryStrategyRepository implements RetryStrategyRepository {

    @Override
    public RetryStrategy strategyOf(String id) {
        return new MockRetryStrategy();
    }
}
