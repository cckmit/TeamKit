package org.team4u.base.filter.v2;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FilterChainTest {

    @Test
    public void doFilter() {
        FilterChain<List<String>, Filter<List<String>>> chain = FilterChain.create(new A(), new B(), new C());
        List<String> context = new ArrayList<>();
        chain.doFilter(context);
        Assert.assertEquals("[A, B]", context.toString());
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
}