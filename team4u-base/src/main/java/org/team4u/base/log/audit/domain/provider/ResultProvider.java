package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 结果信息提供者
 *
 * @author jay.wu
 */
public interface ResultProvider {

    /**
     * 获取结果
     *
     * @param context 上下文
     * @return 结果
     */
    String resultOf(AuditLogContext context);
}