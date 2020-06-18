package org.team4u.core.log;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.core.log.LogMessage;

/**
 * @author Jay Wu
 */
public class LogMessageTest {

    @Test
    public void emptyKeyValues() {
        LogMessage message = newLogMessage().success();
        Assert.assertEquals("TEST|view|succeeded", message.toString());
    }

    @Test
    public void oneKeyValues() {
        LogMessage message = newLogMessage()
                .fail("y")
                .append("x", "1");
        Assert.assertEquals("TEST|view|failed|errorMessage=y|x=1", message.toString());
    }

    @Test
    public void multipleKeyValues() {
        LogMessage message = newLogMessage()
                .processing()
                .append("x", "1")
                .append("y", "3")
                .append("y", "2");
        Assert.assertEquals("TEST|view|processing|x=1|y=2", message.toString());
    }

    @Test
    public void copy() {
        LogMessage message = newLogMessage();
        Assert.assertEquals("TEST|view|processing", message.copy().processing().toString());
        Assert.assertEquals("TEST|view|succeeded", message.copy().success().toString());
    }

    private LogMessage newLogMessage() {
        return new LogMessage("TEST", "view");
    }
}