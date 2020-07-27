package org.team4u.base.sm;

import org.junit.Assert;
import org.junit.Test;

public class AbstractSimpleStateMachineTest {

    @Test
    public void fire() {
        A a = new A();
        TestSm sm = new TestSm();
        sm.registerTransition("1", "c", "b");
        sm.fire(a, "1", "c");
        Assert.assertEquals("b", a.getStatus());
    }

    @Test
    public void fireWithError() {
        A a = new A();
        TestSm sm = new TestSm();
        sm.registerTransition("1", "c", "b");
        sm.registerTransition("2", "a", "b");

        try {
            sm.fireAndCheck(a, "1", "a");
            Assert.fail();
        } catch (IllegalStateException e) {
            // ignore error
        }

    }

    public static class TestSm extends AbstractSimpleStateMachine<A, String, String> {

        @Override
        public void transform(String from, String to, String action, A a) {
            a.setStatus(to);
        }
    }

    public static class A {

        private String status;

        public String getStatus() {
            return status;
        }

        public A setStatus(String status) {
            this.status = status;
            return this;
        }
    }
}