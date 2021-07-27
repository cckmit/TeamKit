package org.team4u.base.lang;

import org.junit.Assert;
import org.junit.Test;

public class SimplePolicyServiceTest {
    private final Service s = new Service() {{
        registerBySuper(SimplePolicyServiceTest.class.getPackage().getName(), SimplePolicy.class);
    }};

    @Test
    public void policyOf() {
        Assert.assertEquals(P1.class, s.policyOf("1").getClass());
        Assert.assertEquals(P2.class, s.policyOf("2").getClass());
    }

    @Test
    public void policiesOf() {
        Assert.assertEquals(2, s.policiesOf("1").size());
        Assert.assertEquals(1, s.policiesOf("2").size());
    }

    public static class Service extends SimplePolicyService<String, P1> {
    }


    public static class P1 implements SimplePolicy<String> {

        @Override
        public boolean isSupport(String context) {
            return context.equals("1");
        }

        @Override
        public void execute(String context) {

        }
    }

    public static class P2 implements SimplePolicy<String> {

        @Override
        public boolean isSupport(String context) {
            return context.equals("2");
        }

        @Override
        public void execute(String context) {

        }
    }

    public static class P3 implements SimplePolicy<String> {

        @Override
        public boolean isSupport(String context) {
            return context.equals("1");
        }

        @Override
        public void execute(String context) {

        }
    }
}