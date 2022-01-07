package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.log.audit.domain.Operator;

/**
 * 操作人提供者
 *
 * @author jay.wu
 */
public interface OperatorProvider extends LogPropProvider {

    /**
     * 获取操作人
     *
     * @param context 上下文
     * @return 操作人
     */
    Operator operatorOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return OperatorProvider.class;
    }
}