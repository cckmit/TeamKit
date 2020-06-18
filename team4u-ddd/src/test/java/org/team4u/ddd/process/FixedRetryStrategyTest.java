package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.FixedRetryStrategy;
import org.junit.Assert;
import org.junit.Test;

public class FixedRetryStrategyTest {

    @Test
    public void calculate() {
        FixedRetryStrategy c = new FixedRetryStrategy(
                new FixedRetryStrategy.Config().setIntervalSec(1)
        );

        Assert.assertEquals(1, c.nextIntervalSec(0));
        Assert.assertEquals(1, c.nextIntervalSec(100));
    }
}