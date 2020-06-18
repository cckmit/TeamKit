package org.team4u.ddd.process.retry.method.annotation;

import java.lang.annotation.*;

/**
 * 开启重试功能
 * <p>
 * 在需要重试的类上开启，使用需要配合 @Retryable 使用
 *
 * @author jay.wu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EnableRetry {
}