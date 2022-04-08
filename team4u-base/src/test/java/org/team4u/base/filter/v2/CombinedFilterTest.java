package org.team4u.base.filter.v2;

import org.junit.Assert;
import org.junit.Test;

public class CombinedFilterTest {

    @Test
    public void create() {
        StringBuffer result = (StringBuffer) FilterChain.create(FilterChain.Config.builder()
                        .name("test")
                        .filter(new A())
                        .filter(CombinedFilter.create(FilterChain.Config.builder()
                                .name("test-1")
                                .filter(new B())
                                .filter(new A())
                                .build()))
                        .filter(CombinedFilter.create(FilterChain.Config.builder()
                                .name("test-2")
                                .filter(new A())
                                .build()))
                        .filter(new C())
                        .build())
                .doFilter(new StringBuffer());

        Assert.assertEquals("1213", result.toString());
    }

    private static class A implements Filter<StringBuffer> {

        @Override
        public boolean doFilter(StringBuffer context) {
            context.append("1");
            return true;
        }
    }

    private static class B implements Filter<StringBuffer> {

        @Override
        public boolean doFilter(StringBuffer context) {
            context.append("2");
            return false;
        }
    }

    private static class C implements Filter<StringBuffer> {

        @Override
        public boolean doFilter(StringBuffer context) {
            context.append("3");
            return true;
        }
    }
}