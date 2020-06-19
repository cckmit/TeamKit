package org.team4u.command.handler;

import org.team4u.command.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.core.lang.EasyMap;

import static org.team4u.command.handler.HandlerAttributesKeyNames.TARGET_KEY;
import static org.team4u.command.handler.HandlerAttributesKeys.TARGET;

public class AbstractDefaultHandlerTest {

    @Test
    public void handleWithDefault() {
        check(null);
    }

    @Test
    public void handleWithSpecific() {
        check("test");
    }

    private void check(String resultKey) {
        FakeHandler fakeHandler = new FakeHandler();
        EasyMap attrs = new EasyMap();
        fakeHandler.handle(
                new EasyMap().set("name", "test")
                        .set(TARGET_KEY, resultKey),
                attrs
        );
        Assert.assertEquals("test", attrs.getStr(resultKey == null ? TARGET : resultKey));
    }

    private static class FakeHandler extends AbstractDefaultHandler<Config, String> {

        public FakeHandler() {
            super(TestUtil.newTemplateEngine(), TestUtil.newInterceptorService());
        }

        @Override
        public String id() {
            return "test";
        }

        @Override
        protected String internalHandle(Config config, EasyMap attributes) {
            return config.getName();
        }
    }

    public static class Config {
        private String name;

        public String getName() {
            return name;
        }

        public Config setName(String name) {
            this.name = name;
            return this;
        }
    }
}