package org.team4u.ddd.infrastructure.process.spring;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.team4u.base.log.LogMessages;
import org.team4u.ddd.process.retry.method.interceptor.RetryInterceptor;
import org.team4u.ddd.process.retry.method.interceptor.RetryInterceptorContext;

import java.lang.reflect.Method;

/**
 * 基于spring的可重试方法拦截器
 *
 * @author jay.wu
 */
@Aspect
public class SpringAspectRetryableMethodInterceptor implements ApplicationContextAware {

    private final Log log = LogFactory.get();
    private final RetryInterceptor retryInterceptor;
    private ApplicationContext applicationContext;

    public SpringAspectRetryableMethodInterceptor(RetryInterceptor retryInterceptor) {
        this.retryInterceptor = retryInterceptor;
    }

    @Around("@annotation(org.team4u.ddd.process.retry.method.annotation.Retryable)")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        return retryInterceptor.invoke(
                new RetryInterceptorContext(
                        new SpringAopInvoker(joinPoint),
                        method,
                        joinPoint.getArgs()
                )
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class SpringAopInvoker implements RetryInterceptorContext.Invoker {

        private final ProceedingJoinPoint joinPoint;

        private SpringAopInvoker(ProceedingJoinPoint joinPoint) {
            this.joinPoint = joinPoint;
        }

        @Override
        public String id() {
            String beanName = ArrayUtil.firstNonNull(applicationContext.getBeanNamesForType(
                    joinPoint.getSignature().getDeclaringType()
            ));

            if (StrUtil.isEmpty(beanName)) {
                log.warn(LogMessages.create(this.getClass().getSimpleName(), "beanNameOf")
                        .fail("beanName is null")
                        .append("beanClass", joinPoint.getSignature().getDeclaringType().getName())
                        .append("methodName", joinPoint.getSignature().getName())
                        .toString());

                return null;
            }

            return beanName;
        }

        @Override
        public Object invoke() throws Throwable {
            return joinPoint.proceed();
        }
    }
}