package org.team4u.base.aop;

import cn.hutool.core.util.ReflectUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;

public class SimpleAop {

    public static <T> T proxyOf(Class<T> superClass,
                                ElementMatcher<? super MethodDescription> matcher,
                                MethodInterceptor interceptor) {
        return ReflectUtil.newInstance(proxyClassOf(superClass, matcher, interceptor));
    }

    public static <T> Class<? extends T> proxyClassOf(Class<T> superClass,
                                                      ElementMatcher<? super MethodDescription> matcher,
                                                      Object methodDelegation) {
        return new ByteBuddy()
                .subclass(superClass)
                .method(matcher)
                .intercept(MethodDelegation.to(methodDelegation))
                .make()
                .load(superClass.getClassLoader())
                .getLoaded();
    }

    public static <T> Class<? extends T> proxyClassOf(Class<T> superClass,
                                                      ElementMatcher<? super MethodDescription> matcher,
                                                      MethodInterceptor interceptor) {
        return proxyClassOf(superClass, matcher, new AopMethodDelegation(interceptor));
    }
}