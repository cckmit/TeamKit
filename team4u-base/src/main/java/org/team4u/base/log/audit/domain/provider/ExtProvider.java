package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

import java.util.Map;

/**
 * 扩展信息提供者
 *
 * @author jay.wu
 */
public interface ExtProvider {

    /**
     * 获取扩展信息
     *
     * @param context 上下文
     * @return 扩展信息
     */
    Map<String, Object> extOf(AuditLogContext context);
}