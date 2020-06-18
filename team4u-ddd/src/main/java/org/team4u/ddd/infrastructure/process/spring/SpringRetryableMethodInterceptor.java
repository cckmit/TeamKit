package org.team4u.ddd.infrastructure.process.spring;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.ddd.process.retry.method.interceptor.RetryInterceptor;
import org.team4u.ddd.process.retry.method.interceptor.RetryInterceptorContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.team4u.core.log.LogMessages;

/**
 * 基于spring的可重试方法拦截器
 *
 * @author jay.wu
 */
public class SpringRetryableMethodInterceptor implements MethodInterceptor, ApplicationContextAware {

    private final Log log = LogFactory.get();
    private final RetryInterceptor retryInterceptor;
    private ApplicationContext applicationContext;

    public SpringRetryableMethodInterceptor(RetryInterceptor retryInterceptor) {
        this.retryInterceptor = retryInterceptor;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return retryInterceptor.invoke(
                new RetryInterceptorContext(
                        new SpringAopInvoker(methodInvocation),
                        methodInvocation.getMethod(),
                        methodInvocation.getArguments()
                )
        );
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class SpringAopInvoker implements RetryInterceptorContext.Invoker {

        private final MethodInvocation methodInvocation;

        private SpringAopInvoker(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public String id() {
            String beanName = ArrayUtil.firstNonNull(applicationContext.getBeanNamesForType(
                    methodInvocation.getThis().getClass()
            ));

            if (StrUtil.isEmpty(beanName)) {
                log.warn(LogMessages.create(this.getClass().getSimpleName(), "beanNameOf")
                        .fail("beanName is null")
                        .append("beanClass", methodInvocation.getThis().getClass().getName())
                        .append("methodName", methodInvocation.getMethod().getName())
                        .toString());

                return null;
            }

            return beanName;
        }

        @Override
        public Object invoke() throws Throwable {
            return methodInvocation.proceed();
        }
    }
}