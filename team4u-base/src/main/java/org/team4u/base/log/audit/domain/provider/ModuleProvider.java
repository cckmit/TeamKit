package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.log.audit.domain.LogModule;

/**
 * 模块信息提供者
 *
 * @author jay.wu
 */
public interface ModuleProvider extends LogPropProvider {

    /**
     * 获取模块
     *
     * @param context 上下文
     * @return 模块对象
     */
    LogModule moduleOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return ModuleProvider.class;
    }
}