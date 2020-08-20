package org.team4u.base.aop;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AopMethodDelegation {

    private final MethodInterceptor interceptor;

    public AopMethodDelegation(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @RuntimeType
    public Object delegate(@SuperCall Callable<?> superMethod,
                           @This Object obj,
                           @Origin Method method,
                           @AllArguments Object[] allArguments) throws Exception {
        return interceptor.intercept(obj, allArguments, method, superMethod);
    }
}