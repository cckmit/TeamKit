package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.ExponentialRandom2RetryStrategy;
import org.junit.Assert;
import org.junit.Test;

public class ExponentialRandom2RetryStrategyTest {

    @Test
    public void calculate() {
        ExponentialRandom2RetryStrategy c = new ExponentialRandom2RetryStrategy(
                new ExponentialRandom2RetryStrategy.Config().setExponentialBase(3)
        );

        Assert.assertTrue(c.nextIntervalSec(0) >= 3);
        Assert.assertTrue(c.nextIntervalSec(0) < 9);
        Assert.assertTrue(c.nextIntervalSec(1) >= 9);
        Assert.assertTrue(c.nextIntervalSec(1) < 27);
    }
}