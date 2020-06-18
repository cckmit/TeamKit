package org.team4u.ddd.infrastructure.process.spring;

import org.team4u.ddd.process.retry.method.annotation.EnableRetry;
import org.springframework.aop.ClassFilter;

/**
 * 可重试类过滤器
 *
 * @author jay.wu
 */
public class RetryableClassFilter implements ClassFilter {

    @Override
    public boolean matches(Class<?> clazz) {
        return clazz.getAnnotation(EnableRetry.class) != null;
    }
}