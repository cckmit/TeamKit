package org.team4u.base.log.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 审计日志
 *
 * @author jay.wu
 */
@Data
@Builder
public class AuditLog {

    private String referenceId;
    private String systemId;
    private LogModule module;
    private Operator operator;
    private Date occurredOn;
    private LogAction action;
    private String ip;
    private String result;
    private String description;
    private Map<String, Object> ext;
}