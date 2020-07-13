package org.team4u.command.handler;

import org.team4u.command.Handler;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.command.config.CommandConfig;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;
import org.team4u.template.infrastructure.function.StringTemplateFunction;

import static org.team4u.command.TestUtil.newInterceptorService;
import static org.team4u.command.TestUtil.newTemplateEngine;

public class AbstractHandlerTest {

    @Test
    public void handle() {
        FakeHandler handler = new FakeHandler(
                newTemplateEngine(new StringTemplateFunction()),
                newInterceptorService()
        );

        CommandConfig config = new CommandConfig();
        config.getHandlers().add(new EasyMap().set("test", new EasyMap().set("x", "${x}").set("y", "${y}")));

        EasyMap attributes = new EasyMap();
        attributes.set("y", 1).set("x", 1);

        Handler.Context context = new Handler.Context(
                attributes,
                config
        );

        //noinspection unchecked
        handler.doFilter(context, FilterInvoker.EMPTY_INVOKER);

        Assert.assertEquals(3, context.attributes().size());
        Assert.assertEquals("11", context.attributes().getStr("x"));
        Assert.assertTrue(context.attributes().getBool("handler"));
    }

    private static class FakeHandler extends AbstractHandler {

        public FakeHandler(TemplateEngine templateEngine,
                           HandlerInterceptorService handlerInterceptorService) {
            super(templateEngine, handlerInterceptorService);
        }

        @Override
        public String id() {
            return "test";
        }

        @Override
        protected void handle(EasyMap config, EasyMap attributes) {
            attributes.set("handler", true);
            attributes.set("x", config.getStr("x") + config.getStr("y"));
        }
    }
}