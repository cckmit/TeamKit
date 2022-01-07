package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 描述信息提供者
 *
 * @author jay.wu
 */
public interface DescriptionProvider extends LogPropProvider {

    /**
     * 获取描述
     *
     * @param context 上下文
     * @return 描述
     */
    String descriptionOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return DescriptionProvider.class;
    }
}