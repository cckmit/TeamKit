package org.team4u.base.log.audit.domain.annotion;

import org.team4u.base.log.audit.domain.condition.TrueCondition;

import java.lang.annotation.*;

/**
 * 跟踪审计日志
 *
 * @author jay.wu
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TraceAuditLog {
    /**
     * 模块
     */
    String module();

    /**
     * 动作
     */
    String action();

    /**
     * 关联资源标识
     */
    String referenceId() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 激活条件标识
     */
    String conditionId() default TrueCondition.ID;
}