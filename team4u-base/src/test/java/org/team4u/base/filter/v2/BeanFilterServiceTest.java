package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.TestUtil;
import org.team4u.base.bean.event.ApplicationInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.jvm.MessagePublisher;

import java.util.List;
import java.util.stream.Collectors;

public class BeanFilterServiceTest {

    private final List<? extends Filter<Dict>> filters = CollUtil.newArrayList(
            new A(), new C(), new B(), new D(), new E()
    );

    private BeanFilterService<Dict, Filter<Dict>> service;

    @Before
    public void before() throws Exception {
        MessagePublisher.instance().reset();

        service = new TestService();

        for (Filter<Dict> filter : filters) {
            BeanProviders.getInstance().local().registerBean(filter);
        }

        MessagePublisher.instance().publish(new ApplicationInitializedEvent());
    }

    @Test
    public void doFilter() {
        Dict c1 = service.doFilter(Dict.create());
        Assert.assertEquals(TestUtil.TEST1 + TestUtil.TEST2, c1.get(TestUtil.TEST));

        Dict c2 = service.doFilter(Dict.create());
        Assert.assertEquals(c1, c2);
    }

    public class TestService extends BeanFilterService<Dict, Filter<Dict>> {

        @SuppressWarnings("unchecked")
        @Override
        protected FilterChain.Config config() {
            return FilterChain.Config.builder()
                    .filterClasses(filters.stream()
                            .map(it -> (Class<Filter<Dict>>) it.getClass())
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    public static class A implements Filter<Dict> {

        @Override
        public boolean doFilter(Dict context) {
            context.set(TestUtil.TEST, TestUtil.TEST1);
            return true;
        }
    }

    public static class B implements Filter<Dict> {

        @Override
        public boolean doFilter(Dict context) {
            context.set(TestUtil.TEST, context.get(TestUtil.TEST) + TestUtil.TEST2);
            return true;
        }
    }

    public static class C implements Filter<Dict> {

        @Override
        public boolean doFilter(Dict context) {
            return true;
        }
    }

    public static class D implements Filter<Dict> {

        @Override
        public boolean doFilter(Dict context) {
            return false;
        }
    }

    public static class E implements Filter<Dict> {

        @Override
        public boolean doFilter(Dict context) {
            context.set(TestUtil.TEST, context.get(TestUtil.TEST) + TestUtil.TEST2);
            return true;
        }
    }
}