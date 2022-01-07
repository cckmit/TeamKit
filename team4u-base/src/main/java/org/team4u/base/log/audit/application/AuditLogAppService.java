package org.team4u.base.log.audit.application;

import cn.hutool.core.convert.Convert;
import lombok.Builder;
import lombok.Getter;
import org.team4u.base.log.audit.domain.*;
import org.team4u.base.log.audit.domain.condition.ConditionHandlerHolder;
import org.team4u.base.log.audit.domain.provider.*;
import org.team4u.base.message.MessagePublisher;

import java.util.Map;

/**
 * 审计日志应用服务
 *
 * @author jay.wu
 */
public class AuditLogAppService {

    private final Providers providers;
    private final ConditionHandlerHolder conditionHandlerHolder;

    public AuditLogAppService(Providers providers) {
        this.providers = providers;
        this.conditionHandlerHolder = new ConditionHandlerHolder();
    }

    /**
     * 创建审计日志
     *
     * @param context 上下文
     * @return 日志对象
     */
    public AuditLog create(AuditLogContext context) {
        AuditLog auditLog = AuditLog.builder()
                .ip(ipOf(context))
                .ext(extOf(context))
                .action(actionOf(context))
                .module(moduleOf(context))
                .result(resultOf(context))
                .operator(operatorOf(context))
                .referenceId(referenceIdOf(context))
                .description(descriptionOf(context))
                .occurredOn(context.getOccurredOn())
                .build();

        MessagePublisher.instance().publish(new AuditLogCreatedEvent(auditLog));

        return auditLog;
    }

    /**
     * 判断是否需要跟踪
     *
     * @param context 上下文
     * @return true：需要跟踪，false：不需要跟踪
     */
    public boolean canTrace(AuditLogContext context) {
        return conditionHandlerHolder.test(context);
    }

    private String descriptionOf(AuditLogContext context) {
        if (providers.getDescriptionProvider() == null) {
            return context.getDescription();
        }

        return providers.getDescriptionProvider().descriptionOf(context);
    }

    private String ipOf(AuditLogContext context) {
        if (providers.getIpProvider() == null) {
            return null;
        }

        return providers.getIpProvider().ipOf(context);
    }

    private String resultOf(AuditLogContext context) {
        if (providers.getResultProvider() == null) {
            return Convert.toStr(context.getReturnValue());
        }

        return providers.getResultProvider().resultOf(context);
    }

    private LogModule moduleOf(AuditLogContext context) {
        if (providers.getModuleProvider() == null) {
            return new LogModule(context.getModuleId(), context.getModuleId());
        }

        return providers.getModuleProvider().moduleOf(context);
    }

    private LogAction actionOf(AuditLogContext context) {
        if (providers.getActionProvider() == null) {
            return new LogAction(context.getActionId(), context.getActionId());
        }

        return providers.getActionProvider().actionOf(context);
    }

    private String referenceIdOf(AuditLogContext context) {
        if (providers.getReferenceIdProvider() == null) {
            return context.getReferenceId();
        }

        return providers.getReferenceIdProvider().referenceIdOf(context);
    }

    private Operator operatorOf(AuditLogContext context) {
        if (providers.getOperatorProvider() == null) {
            return Operator.SYSTEM;
        }

        return providers.getOperatorProvider().operatorOf(context);
    }

    private Map<String, Object> extOf(AuditLogContext context) {
        if (providers.getExtProvider() == null) {
            return AuditLogContext.ext();
        }

        return providers.getExtProvider().extOf(context);
    }

    /**
     * 日志信息提供者服务
     */
    @Getter
    @Builder
    public static class Providers {
        private IpProvider ipProvider;
        private ExtProvider extProvider;
        private ModuleProvider moduleProvider;
        private ActionProvider actionProvider;
        private ResultProvider resultProvider;
        private OperatorProvider operatorProvider;
        private ReferenceIdProvider referenceIdProvider;
        private DescriptionProvider descriptionProvider;
    }
}