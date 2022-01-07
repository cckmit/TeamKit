package org.team4u.base.log.audit.domain.condition;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 假条件
 *
 * @author jay.wu
 */
public class FalseCondition implements ConditionHandler {

    public static final String ID = "FALSE";

    @Override
    public boolean test(AuditLogContext context) {
        return true;
    }

    @Override
    public String id() {
        return ID;
    }
}