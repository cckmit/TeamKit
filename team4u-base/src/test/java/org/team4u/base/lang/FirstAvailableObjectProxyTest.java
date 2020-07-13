package org.team4u.base.lang;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.UndeclaredThrowableException;

public class FirstAvailableObjectProxyTest {

    @Test
    public void first() {
        Testable testable = FirstAvailableObjectProxy.newProxyInstance(
                Testable.class,
                CollUtil.newArrayList(
                        new MockTest("1"),
                        new MockTest("2")
                ),
                true
        );

        Assert.assertEquals("1", testable.id());
    }

    @Test
    public void second() {
        Testable testable = FirstAvailableObjectProxy.newProxyInstance(
                Testable.class,
                CollUtil.newArrayList(
                        new MockTest(null),
                        new MockTest("2")
                ),
                true
        );

        Assert.assertEquals("2", testable.id());
    }

    @Test
    public void oneError() {
        Testable testable = FirstAvailableObjectProxy.newProxyInstance(
                Testable.class,
                CollUtil.newArrayList(
                        new MockTest(true),
                        new MockTest("2")
                ),
                true
        );

        Assert.assertEquals("2", testable.id());
    }

    @Test
    public void allError() {
        Testable testable = FirstAvailableObjectProxy.newProxyInstance(
                Testable.class,
                CollUtil.newArrayList(
                        new MockTest(true),
                        new MockTest(true)
                ),
                true
        );

        try {
            testable.id();
            Assert.fail();
        } catch (UndeclaredThrowableException e) {
            // ignore error
        }
    }

    @Test
    public void notIgnoreError() {
        Testable testable = FirstAvailableObjectProxy.newProxyInstance(
                Testable.class,
                CollUtil.newArrayList(
                        new MockTest(true),
                        new MockTest("1")
                ),
                false
        );

        try {
            testable.id();
            Assert.fail();
        } catch (UndeclaredThrowableException e) {
            // ignore error
        }
    }

    public interface Testable {

        String id();
    }

    public static class MockTest implements Testable {

        private final String id;

        private final boolean shouldError;

        public MockTest(String id) {
            this(id, false);
        }

        public MockTest(boolean shouldError) {
            this(null, shouldError);
        }

        public MockTest(String id, boolean shouldError) {
            this.id = id;
            this.shouldError = shouldError;
        }

        @Override
        public String id() {
            if (shouldError) {
                throw new RuntimeException("1");
            }

            return id;
        }
    }
}