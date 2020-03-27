package org.team4u.kit.core.log;

import org.junit.Assert;
import org.junit.Test;

public class SimpleLogMessageRenderTest {

    @Test
    public void render() {
        LogMessage lm = new LogMessage("X", "y");
        lm.config().setMinSpendTimeMillsToDisplay(1);
        lm.append("a", 1);
        lm.success();
        lm.setCompletedTime(lm.processingTime() + 5);
        Assert.assertEquals("X|y|succeeded|5ms|a=1", lm.toString());
    }
}