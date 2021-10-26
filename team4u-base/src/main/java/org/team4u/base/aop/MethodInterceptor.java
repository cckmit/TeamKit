package org.team4u.base.aop;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface MethodInterceptor {

    Object intercept(Object instance,
                     Object[] parameters,
                     Method method,
                     Callable<?> superMethod) throws Throwable;
}