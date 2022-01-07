package org.team4u.base.log.audit.domain.condition;

import org.team4u.base.log.audit.domain.AuditLogContext;

/**
 * 真条件
 *
 * @author jay.wu
 */
public class TrueCondition implements ConditionHandler {

    public static final String ID = "TRUE";

    @Override
    public boolean test(AuditLogContext context) {
        return true;
    }

    @Override
    public String id() {
        return ID;
    }
}