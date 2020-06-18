package org.team4u.core.aop;

import cn.hutool.aop.aspects.Aspect;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Spring-cglib实现的动态代理切面
 *
 * @author looly
 */
public class SpringCglibInterceptor implements MethodInterceptor, Serializable {
    private static final long serialVersionUID = 1L;

    private final Object target;
    private final Aspect aspect;

    /**
     * 构造
     *
     * @param target 被代理对象
     * @param aspect 切面实现
     */
    public SpringCglibInterceptor(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    /**
     * 获得目标对象
     *
     * @return 目标对象
     */
    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        final Object target = this.target;
        Object result = null;
        // 开始前回调
        if (aspect.before(target, method, args)) {
            try {
//				result = proxy.invokeSuper(obj, args);
                result = proxy.invoke(target, args);
            } catch (Throwable e) {
                if (aspect.afterException(target, method, args, e)) {
                    throw e;
                }
            }
        }

        // 结束执行回调
        if (aspect.after(target, method, args, result)) {
            return result;
        }
        return null;
    }
}
