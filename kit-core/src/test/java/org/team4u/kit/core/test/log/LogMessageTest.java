package org.team4u.kit.core.test.log;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.log.LogMessage;

/**
 * @author Jay Wu
 */
public class LogMessageTest {

    @Test
    public void emptyKeyValues() {
        LogMessage message = newLogMessage().success();
        Assert.assertEquals("TEST|view|success", message.toString());
    }

    @Test
    public void oneKeyValues() {
        LogMessage message = newLogMessage()
                .fail("y")
                .append("x", "1");
        Assert.assertEquals("TEST|view|fail|\"errorMessage\"=\"y\"|\"x\"=\"1\"", message.toString());
    }

    @Test
    public void multipleKeyValues() {
        LogMessage message = newLogMessage()
                .processing()
                .append("x", "1")
                .append("y", "2");
        Assert.assertEquals("TEST|view|processing|\"x\"=\"1\"|\"y\"=\"2\"", message.toString());
    }

    @Test
    public void copy() {
        LogMessage message = newLogMessage();
        Assert.assertEquals("TEST|view|processing|\"x\"=\"1\"",
                message.copy().processing().append("x", "1").toString());
        Assert.assertEquals("TEST|view|success|\"y\"=\"2\"",
                message.copy().success().append("y", "2").toString());
    }

    @Test
    public void appendWithEscape() {
        LogMessage message = newLogMessage()
                .processing()
                .appendWithEscape("x", "我");
        Assert.assertEquals("TEST|view|processing|\"x\"=\"%u6211\"", message.toString());
    }

    private LogMessage newLogMessage() {
        return new LogMessage("TEST", "view");
    }
}