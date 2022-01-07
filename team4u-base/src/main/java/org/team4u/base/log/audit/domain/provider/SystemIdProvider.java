package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 系统标识提供者
 *
 * @author jay.wu
 */
public interface SystemIdProvider extends LogPropProvider {

    /**
     * 获取系统标识
     *
     * @param context 上下文
     * @return 系统标识
     */
    String systemIdOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return SystemIdProvider.class;
    }
}