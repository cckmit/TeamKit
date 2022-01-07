package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 关联标识提供者
 *
 * @author jay.wu
 */
public interface ReferenceIdProvider extends LogPropProvider {

    /**
     * 获取关联标识
     *
     * @param context 上下文
     * @return 关联标识
     */
    String referenceIdOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return ReferenceIdProvider.class;
    }
}