package org.team4u.command;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.VoidFunc1;
import org.team4u.command.config.CommandConfig;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.core.error.SystemDataNotExistException;
import org.team4u.core.filter.FilterInvoker;
import org.team4u.core.lang.EasyMap;

public class HandlerInterceptorServiceTest {

    @Test
    public void skipInterceptor() {
        Handler.Context context = apply(new FakeHandlerInterceptor(false));
        Assert.assertEquals(1, context.attributes().size());
        Assert.assertTrue(context.attributes().getBool("completion"));
    }

    @Test
    public void noInterceptor() {
        try {
            Handler.Context context = apply();
            Assert.fail();
        } catch (SystemDataNotExistException e) {
            Assert.assertEquals("FilterInterceptorId=test", e.getMessage());
        }
    }

    @Test
    public void normalInterceptor() {
        Handler.Context context = apply(new FakeHandlerInterceptor(true));
        Assert.assertEquals(3, context.attributes().size());
        Assert.assertTrue(context.attributes().getBool("work"));
        Assert.assertTrue(context.attributes().getBool("post"));
        Assert.assertTrue(context.attributes().getBool("completion"));
    }

    @Test
    public void errorInterceptor() {
        try {
            apply((c) -> {
                throw new FakeRuntimeException(c);
            }, new FakeHandlerInterceptor(true));
            Assert.fail();
        } catch (FakeRuntimeException e) {
            Handler.Context context = e.getContext();

            Assert.assertEquals(1, context.attributes().size());
            Assert.assertTrue(context.attributes().getBool("completion"));
        }
    }

    private Handler.Context apply(HandlerInterceptor... interceptors) {
        return apply((c) -> c.attributes().set("work", true), interceptors);
    }

    private Handler.Context apply(VoidFunc1<Handler.Context> worker, HandlerInterceptor... interceptors) {
        HandlerInterceptorService service = new HandlerInterceptorService(CollUtil.toList(interceptors));
        Handler.Context context = new Handler.Context(
                new EasyMap(),
                new CommandConfig().setInterceptors(CollUtil.newArrayList("test"))
        );
        service.apply(context, new FakeHandler(), worker);
        return context;
    }

    private static class FakeHandler implements Handler {

        @Override
        public String id() {
            return null;
        }

        @Override
        public void doFilter(Context context, FilterInvoker<Context> nextFilter) {
            context.attributes().set("handler", true);
        }
    }

    private static class FakeHandlerInterceptor implements HandlerInterceptor {

        private final boolean canGo;

        private FakeHandlerInterceptor(boolean canGo) {
            this.canGo = canGo;
        }

        @Override
        public boolean preHandle(Handler.Context context, Handler handler) throws Exception {
            return canGo;
        }

        @Override
        public void postHandle(Handler.Context context, Handler handler) throws Exception {
            context.attributes().set("post", true);
        }

        @Override
        public void afterCompletion(Handler.Context context, Handler handler, Exception ex) throws Exception {
            context.attributes().set("completion", true);
        }

        @Override
        public String id() {
            return "test";
        }
    }

    private static class FakeRuntimeException extends RuntimeException {
        private final Handler.Context context;

        private FakeRuntimeException(Handler.Context context) {
            this.context = context;
        }

        public Handler.Context getContext() {
            return context;
        }
    }
}