package org.team4u.base.log.audit.domain;

import lombok.Data;

/**
 * 审计日志创建事件
 *
 * @author jay.wu
 */
@Data
public class AuditLogCreatedEvent {

    private final AuditLog auditLog;
}