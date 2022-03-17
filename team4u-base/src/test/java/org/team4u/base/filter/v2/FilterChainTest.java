package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.bean.provider.LocalBeanProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FilterChainTest {

    private final TestFilterInterceptor testFilterInterceptor = new TestFilterInterceptor();

    @After
    public void clear() {
        LocalBeanProvider provider = (LocalBeanProvider) BeanProviders.getInstance().policyOf(LocalBeanProvider.ID);
        provider.unregisterAllBeans();
    }

    @Test
    public void doFilterWithBeans() {
        FilterChain<List<String>, Filter<List<String>>> chain = new FilterChain<>(
                FilterChain.Config.builder()
                        .filters(beans())
                        .interceptor(testFilterInterceptor)
                        .build()
        );

        check(chain);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void doFilterWithBeanCLasses() {
        List<Class<? extends Filter<?>>> f = beans().stream()
                .filter(it -> BeanProviders.getInstance().registerBean(it))
                .map(it -> (Class<? extends Filter<?>>) it.getClass())
                .collect(Collectors.toList());

        FilterChain<List<String>, Filter<List<String>>> chain = new FilterChain<>(
                FilterChain.Config.builder()
                        .filterClasses(f)
                        .interceptor(testFilterInterceptor)
                        .build()
        );

        check(chain);
    }

    @Test
    public void error() {
        FilterChain<List<String>, Filter<List<String>>> chain = new FilterChain<>(
                FilterChain.Config.builder().filter(new E()).build()
        );

        try {
            chain.doFilter(Collections.emptyList());
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals(TestUtil.TEST, e.getMessage());
        }
    }

    private void check(FilterChain<List<String>, Filter<List<String>>> chain) {
        List<String> context = chain.doFilter(new ArrayList<>());
        Assert.assertEquals("[A, B]", context.toString());
    }

    private List<Filter<List<String>>> beans() {
        return CollUtil.newArrayList(new A(), new B(), new C(), new E());
    }

    public static class A implements Filter<List<String>> {

        @Override
        public boolean doFilter(List<String> context) {
            context.add("A");
            return true;
        }
    }

    public static class B implements Filter<List<String>> {

        @Override
        public boolean doFilter(List<String> context) {
            context.add("B");
            return false;
        }
    }

    public static class C implements Filter<List<String>> {

        @Override
        public boolean doFilter(List<String> context) {
            context.add("C");
            return true;
        }
    }

    public static class E implements Filter<List<String>> {

        @Override
        public boolean doFilter(List<String> context) {
            throw new RuntimeException(TestUtil.TEST);
        }
    }

    public static class TestFilterInterceptor implements FilterInterceptor<List<String>, Filter<List<String>>> {

        @Override
        public boolean preHandle(List<String> strings, Filter<List<String>> filter) {
            return !(filter instanceof E);
        }

        @Override
        public void postHandle(List<String> strings, Filter<List<String>> filter, boolean toNext) {

        }

        @Override
        public void afterCompletion(List<String> strings, Filter<List<String>> filter, Exception e) {

        }
    }
}