package org.team4u.base.aop;

import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class SimpleAopTest {

    @Test
    public void proxyOf() {
        A a = SimpleAop.proxyOf(
                A.class,
                ElementMatchers.nameMatches("get\\w+"),
                new TestMethodInterceptor()
        );

        Assert.assertEquals("getName", a.getName());
    }

    public static class A {

        public String getName() {
            return null;
        }
    }

    public static class TestMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object instance, Object[] parameters, Method method, Callable<?> superMethod) throws Exception {
            return method.getName();
        }
    }
}