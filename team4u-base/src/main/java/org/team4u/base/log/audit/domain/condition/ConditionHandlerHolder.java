package org.team4u.base.log.audit.domain.condition;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 条件处理器服务
 *
 * @author jay.wu
 */
public class ConditionHandlerHolder extends PolicyRegistrar<String, ConditionHandler> {

    private final Log log = Log.get();

    public ConditionHandlerHolder() {
        registerPolicy(new TrueCondition());
        registerPolicy(new FalseCondition());
        registerPoliciesByBeanProvidersAndEvent();
    }

    /**
     * 测试是否满足条件
     *
     * @param context 上下文
     * @return true:满足条件，false:不满足条件
     */
    public boolean test(AuditLogContext context) {
        boolean result = availablePolicyOf(context.getConditionId()).test(context);

        if (log.isDebugEnabled()) {
            log.debug(LogMessage.create(this.getClass().getSimpleName(), "test")
                    .result(result + "")
                    .toString());
        }

        return result;
    }
}