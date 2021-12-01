package org.team4u.base.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;

public abstract class AbstractSimpleFilterServiceTest {

    protected abstract FilterService<Dict> filterService();

    protected static List<? extends Filter<Dict>> filters = CollUtil.newArrayList(
            new A(), new C(), new B(), new D(), new E()
    );

    @Test
    public void doFilter() {
        FilterService<Dict> service = filterService();
        Dict c = service.doFilter(Dict.create());
        Assert.assertEquals(TestUtil.TEST1 + TestUtil.TEST2, c.get(TestUtil.TEST));

        c = service.doFilter(Dict.create());
        Assert.assertEquals(TestUtil.TEST1 + TestUtil.TEST2, c.get(TestUtil.TEST));
    }

    public static class A extends SequentialFilter<Dict> {

        @Override
        protected void doFilter(Dict context) {
            context.set(TestUtil.TEST, TestUtil.TEST1);
        }
    }

    public static class B extends SequentialFilter<Dict> {

        @Override
        protected void doFilter(Dict context) {
            context.set(TestUtil.TEST, context.get(TestUtil.TEST) + TestUtil.TEST2);
        }
    }

    public static class C extends ConditionalFilter<Dict> {

        @Override
        protected boolean doFilter(Dict context) {
            return true;
        }
    }

    public static class D extends ConditionalFilter<Dict> {

        @Override
        protected boolean doFilter(Dict context) {
            return false;
        }
    }

    public static class E extends SequentialFilter<Dict> {

        @Override
        protected void doFilter(Dict context) {
            context.set(TestUtil.TEST, context.get(TestUtil.TEST) + TestUtil.TEST2);
        }
    }
}