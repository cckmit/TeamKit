package org.team4u.ddd.process;

import org.team4u.ddd.process.strategy.IncrementRetryStrategy;
import org.junit.Assert;
import org.junit.Test;

public class IncrementRetryStrategyTest {

    @Test
    public void calculate() {
        IncrementRetryStrategy c = new IncrementRetryStrategy(
                new IncrementRetryStrategy.Config().setIntervalSec(1)
        );

        Assert.assertEquals(1, c.nextIntervalSec(0));
        Assert.assertEquals(2, c.nextIntervalSec(1));
        Assert.assertEquals(3, c.nextIntervalSec(2));
    }
}