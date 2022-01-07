package org.team4u.base.log.audit.infrastructure.spring;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.team4u.base.log.audit.application.AuditLogAppService;
import org.team4u.base.log.audit.domain.AuditLogContext;
import org.team4u.base.log.audit.domain.annotion.TraceAuditLog;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 审计日志切面
 *
 * @author jay.wu
 */
@Slf4j
@Aspect
public class AuditLogAspect {

    private final AuditLogAppService auditLogAppService;

    public AuditLogAspect(AuditLogAppService auditLogAppService) {
        this.auditLogAppService = auditLogAppService;
    }

    @Around("@annotation(org.team4u.base.log.audit.domain.annotion.TraceAuditLog)")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        AuditLogContext.AuditLogContextBuilder builder = builderOf(method, joinPoint.getArgs());

        try {
            Object returnValue = joinPoint.proceed();
            builder.returnValue(returnValue);
            return returnValue;
        } catch (Throwable e) {
            builder.error(e);
            throw e;
        } finally {
            try {
                auditLogAppService.create(builder.build());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private AuditLogContext.AuditLogContextBuilder builderOf(Method method, Object[] args) {
        TraceAuditLog trace = method.getAnnotation(TraceAuditLog.class);

        return AuditLogContext.builder()
                .method(method)
                .methodArgs(args)
                .referenceId(trace.referenceId())
                .moduleId(trace.module())
                .actionId(trace.action())
                .occurredOn(new Date())
                .description(trace.description());
    }
}