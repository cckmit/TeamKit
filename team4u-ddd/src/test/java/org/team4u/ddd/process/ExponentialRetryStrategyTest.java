package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.ExponentialRetryStrategy;
import org.junit.Assert;
import org.junit.Test;

public class ExponentialRetryStrategyTest {

    @Test
    public void calculate() {
        ExponentialRetryStrategy c = new ExponentialRetryStrategy(
                new ExponentialRetryStrategy.Config().setExponentialBase(3)
        );

        Assert.assertEquals(3, c.nextIntervalSec(0));
        Assert.assertEquals(9, c.nextIntervalSec(1));
    }
}