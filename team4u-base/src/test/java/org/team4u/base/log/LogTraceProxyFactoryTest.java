package org.team4u.base.log;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogTraceProxyFactoryTest {

    private final FakeLogX logX = new FakeLogX();

    @Test
    public void proxy() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig());
        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing|input=jay, A|say|succeeded|output=jay]",
                logX.getMessages().toString());
    }

    @Test
    public void disabledLog() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig().setEnabled(false));
        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertTrue(logX.getMessages().isEmpty());
    }

    @Test
    public void disabledInput() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig().setInputEnabled(false));

        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing, A|say|succeeded|output=jay]",
                logX.getMessages().toString());
    }

    @Test
    public void disabledOutput() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig().setOutputEnabled(false));

        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing|input=jay, A|say|succeeded]",
                logX.getMessages().toString());
    }

    @Test
    public void error() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig());

        try {
            a.error();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof RuntimeException);
            Assert.assertEquals("x", e.getMessage());
        }

        Assert.assertEquals("[A|error|processing, A|error|failed|errorMessage=x]",
                logX.getMessages().toString());
    }

    @Test
    public void mask() {
        A a = LogTraceProxyFactory.proxy(new A(), newConfig());

        a.mask("123456", "123456");

        Assert.assertEquals("[A|mask|processing|input=[\"123456\",\"*\"], A|mask|succeeded|output={\"name\":\"*\"}]",
                logX.getMessages().toString());

        a.mask2(new Request());
        Assert.assertEquals("[A|mask|processing|input=[\"123456\",\"*\"], A|mask|succeeded|output={\"name\":\"*\"}, A|mask2|processing|input={\"name3\":\"*\"}, A|mask2|succeeded]",
                logX.getMessages().toString());
    }

    private LogTraceProxyFactory.Config newConfig() {
        return new LogTraceProxyFactory.Config().setLogX(logX);
    }

    public static class FakeLogX implements LogTraceProxyFactory.LogX {

        private final List<String> messages = new ArrayList<>();

        @Override
        public void info(String format) {
            messages.add(format);
        }

        @Override
        public void error(Throwable t, String format) {
            messages.add(format);
        }

        public List<String> getMessages() {
            return messages;
        }
    }

    public static class A {

        public Dict mask(String name, String name2) {
            return Dict.create().set("name", "fjayblue");
        }

        public String say(String name) {
            return name;
        }

        public void error() {
            throw new RuntimeException("x");
        }

        public void mask2(Request request) {

        }
    }

    @Data
    public static class Request {

        private String name3 = "test";
    }
}
