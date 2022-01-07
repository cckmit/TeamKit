package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * IP信息提供者
 *
 * @author jay.wu
 */
public interface IpProvider {

    /**
     * 获取IP
     *
     * @param context 上下文
     * @return IP
     */
    String ipOf(AuditLogContext context);
}