package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

import java.util.Map;

/**
 * 扩展信息提供者
 *
 * @author jay.wu
 */
public interface ExtProvider extends LogPropProvider {

    /**
     * 获取扩展信息
     *
     * @param context 上下文
     * @return 扩展信息
     */
    Map<String, Object> extOf(AuditLogContext context);

    @Override
    default Class<? extends LogPropProvider> id() {
        return ExtProvider.class;
    }
}