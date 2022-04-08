package org.team4u.base.log;

import java.lang.annotation.*;

/**
 * 是否禁用自动日志打印功能
 *
 * @author jay.wu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DisableAutoLog {
}