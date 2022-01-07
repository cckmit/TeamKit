package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 描述信息提供者
 *
 * @author jay.wu
 */
public interface DescriptionProvider {

    /**
     * 获取描述
     *
     * @param context 上下文
     * @return 描述
     */
    String descriptionOf(AuditLogContext context);
}