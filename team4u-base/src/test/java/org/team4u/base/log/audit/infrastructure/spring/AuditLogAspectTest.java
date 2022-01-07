package org.team4u.base.log.audit.infrastructure.spring;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.team4u.base.log.audit.application.AuditLogAppService;
import org.team4u.base.log.audit.domain.*;
import org.team4u.base.log.audit.domain.annotion.TraceAuditLog;
import org.team4u.base.log.audit.domain.provider.OperatorProvider;
import org.team4u.base.log.audit.domain.provider.ReferenceIdProvider;
import org.team4u.base.message.AbstractMessageSubscriber;
import org.team4u.base.message.MessagePublisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuditLogAspectTest.MockBeanConfig.class)
public class AuditLogAspectTest {

    @Autowired
    private A a;

    @Autowired
    private AuditLogCreatedEventSubscriber subscriber;

    @Test
    public void testInvoke() {
        String returnValue = a.say("a");

        Assert.assertEquals(returnValue, auditLog().getResult());
        Assert.assertEquals(new LogAction("A", "A"), auditLog().getAction());
        Assert.assertEquals(new LogModule("M", "M"), auditLog().getModule());
        Assert.assertEquals("name" + returnValue, auditLog().getReferenceId());
        Assert.assertEquals(new Operator("O"), auditLog().getOperator());
    }

    private AuditLog auditLog() {
        return subscriber.getEvent().getAuditLog();
    }

    @Component
    public static class MockReferenceIdProvider implements ReferenceIdProvider {

        @Override
        public String referenceIdOf(AuditLogContext context) {
            return Convert.toStr(context.getReferenceId() + ArrayUtil.firstNonNull(context.getMethodArgs()));
        }
    }

    @Component
    public static class MockOperatorProvider implements OperatorProvider {

        @Override
        public Operator operatorOf(AuditLogContext context) {
            return new Operator("O");
        }
    }

    @Component
    public static class A {

        @TraceAuditLog(module = "M", action = "A", referenceId = "name")
        public String say(String name) {
            return name;
        }
    }

    @Component
    public static class AuditLogCreatedEventSubscriber extends AbstractMessageSubscriber<AuditLogCreatedEvent> {
        @Getter
        public AuditLogCreatedEvent event;

        @Override
        protected void internalOnMessage(AuditLogCreatedEvent auditLogCreatedEvent) {
            this.event = auditLogCreatedEvent;
        }
    }

    @EnableAspectJAutoProxy
    @Configuration
    @ComponentScan("org.team4u.base.log.audit.infrastructure.spring")
    public static class MockBeanConfig {

        @Bean
        public AuditLogAppService auditLogAppService(AuditLogCreatedEventSubscriber subscriber,
                                                     OperatorProvider operatorProvider,
                                                     ReferenceIdProvider referenceIdProvider) {
            MessagePublisher.instance().subscribe(subscriber);

            return new AuditLogAppService(
                    AuditLogAppService.Providers.builder()
                            .operatorProvider(operatorProvider)
                            .referenceIdProvider(referenceIdProvider)
                            .build()
            );
        }

        @Bean
        public AuditLogAspect auditTraceAspect(AuditLogAppService auditLogAppService) {
            return new AuditLogAspect(auditLogAppService);
        }
    }
}