package org.team4u.base.log.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.log.dialect.console.ConsoleLog;
import cn.hutool.log.level.Level;
import lombok.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.team4u.base.log.LogMessageConfig;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.MaskLogMessageRender;
import org.team4u.test.Benchmark;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLogAopTest {

    private final FakeLogX logX = new FakeLogX();

    private final LogAop logAop = newLogAop();

    protected abstract LogAop newLogAop();

    @Before
    public void setUp() {
        logX.messages.clear();
    }

    @Test
    public void proxy() {
        A a = logAop.proxy(new A(), newConfig().build());
        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing|input=jay, A|say|succeeded|output=jay]",
                logX.getMessages().toString());
    }

    @Test
    public void disabledLog() {
        A a = logAop.proxy(new A(), newConfig().enabled(false).build());
        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertTrue(logX.getMessages().isEmpty());
    }

    @Test
    public void disabledInput() {
        A a = logAop.proxy(new A(), newConfig().inputEnabled(false).build());

        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing, A|say|succeeded|output=jay]",
                logX.getMessages().toString());
    }

    @Test
    public void disabledOutput() {
        A a = logAop.proxy(new A(), newConfig().outputEnabled(false).build());

        Assert.assertEquals("jay", a.say("jay"));
        Assert.assertEquals("[A|say|processing|input=jay, A|say|succeeded]",
                logX.getMessages().toString());
    }

    @Test
    public void error() {
        A a = logAop.proxy(new A(), newConfig().inputEnabled(false).outputEnabled(false).build());
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
    public void includedMethods() {
        A a = logAop.proxy(new A(), newConfig().includedMethods(CollUtil.newArrayList("say")).build());
        Assert.assertEquals("jay", a.say("jay"));
        a.mask("fjay", "blue");
        Assert.assertEquals(
                "[A|say|processing|input=jay, A|say|succeeded|output=jay]",
                logX.getMessages().toString()
        );
    }

    @Test
    public void excludedMethods() {
        A a = logAop.proxy(new A(), newConfig().excludedMethods(CollUtil.newArrayList("mask")).build());
        Assert.assertEquals("jay", a.say("jay"));
        a.mask("fjay", "blue");
        Assert.assertEquals(
                "[A|say|processing|input=jay, A|say|succeeded|output=jay]",
                logX.getMessages().toString()
        );
    }

    @Test
    @Ignore
    public void benchmark() {
        A a = logAop.proxy(new A(), newConfig().enabled(false).build());
        Benchmark benchmark = new Benchmark();
        benchmark.setPrintError(true);
        benchmark.start(5, () -> Assert.assertEquals("jay", a.say("jay")));
    }

    @Test
    public void mask() {
        A a = logAop.proxy(new A(), newConfig().build());

        a.mask("123456", "123456");

        Assert.assertEquals("[A|mask|processing|input=[\"123456\",\"*\"], A|mask|succeeded|output={\"name\":\"*\"}]",
                logX.getMessages().toString());

        a.mask2(new Request());
        Assert.assertEquals("[A|mask|processing|input=[\"123456\",\"*\"], A|mask|succeeded|output={\"name\":\"*\"}, A|mask2|processing|input={\"name3\":\"*\"}, A|mask2|succeeded]",
                logX.getMessages().toString());
    }

    private LogAop.Config.ConfigBuilder newConfig() {
        return LogAop.Config.builder()
                .log(logX)
                .logMessageConfig(
                        new LogMessageConfig()
                                .setLogMessageRender(new MaskLogMessageRender(LogMessages.dynamicMasker))
                                .setMinSpendTimeMillsToDisplay(5000));
    }

    public static class FakeLogX extends ConsoleLog {

        private final List<String> messages = new ArrayList<>();

        public FakeLogX() {
            super(FakeLogX.class);
        }

        @Override
        public void log(String fqcn, Level level, Throwable t, String format, Object... arguments) {
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
