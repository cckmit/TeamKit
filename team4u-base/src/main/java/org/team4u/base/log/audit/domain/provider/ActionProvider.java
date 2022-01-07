package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.log.audit.domain.LogAction;

/**
 * 动作信息提供者
 *
 * @author jay.wu
 */
public interface ActionProvider {

    /**
     * 获取动作
     *
     * @param context 上下文
     * @return 动作对象
     */
    LogAction actionOf(AuditLogContext context);
}