package org.team4u.base.log.audit.domain.condition;

import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.registrar.StringIdPolicy;

/**
 * 条件处理器
 *
 * @author jay.wu
 */
public interface ConditionHandler extends StringIdPolicy {

    /**
     * 测试是否满足条件
     *
     * @param context 上下文
     * @return true:满足条件，false:不满足条件
     */
    boolean test(AuditLogContext context);
}
