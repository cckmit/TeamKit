package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.ExponentialRandomRetryStrategy;
import org.junit.Assert;
import org.junit.Test;

public class ExponentialRandomRetryStrategyTest {

    @Test
    public void calculate() {
        ExponentialRandomRetryStrategy c = new ExponentialRandomRetryStrategy(
                new ExponentialRandomRetryStrategy.Config().setExponentialBase(3)
        );

        Assert.assertTrue(c.nextIntervalSec(0) < 3);
        Assert.assertTrue(c.nextIntervalSec(1) < 9);
    }
}