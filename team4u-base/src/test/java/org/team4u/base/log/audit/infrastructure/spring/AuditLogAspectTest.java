package org.team4u.base.log.audit.infrastructure.spring;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.team4u.base.log.audit.application.AuditLogAppService;
import org.team4u.base.log.audit.domain.*;
import org.team4u.base.log.audit.domain.annotion.TraceAuditLog;
import org.team4u.base.log.audit.domain.condition.ConditionHandler;
import org.team4u.base.log.audit.domain.provider.OperatorProvider;
import org.team4u.base.log.audit.domain.provider.ReferenceIdProvider;
import org.team4u.base.message.AbstractMessageSubscriber;
import org.team4u.base.message.MessagePublisher;
import org.team4u.base.spring.SpringInitializedPublisher;
import org.team4u.test.spring.BaseTestBeanConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AuditLogAspectTest.MockBeanConfig.class)
public class AuditLogAspectTest {

    @Autowired
    private A a;

    @Autowired
    private AuditLogCreatedEventSubscriber subscriber;

    @Before
    public void setUp() throws Exception {
        subscriber.setEvent(null);
    }

    @BeforeClass
    public static void beforeClass() {
        MessagePublisher.instance().reset();
    }

    @Test
    public void trace() {
        String returnValue = a.say("a");

        Assert.assertEquals(returnValue, auditLog().getResult());
        Assert.assertEquals(new LogAction("A", "A"), auditLog().getAction());
        Assert.assertEquals(new LogModule("M", "M"), auditLog().getModule());
        Assert.assertEquals("name" + returnValue, auditLog().getReferenceId());
        Assert.assertEquals(new Operator("O"), auditLog().getOperator());
    }

    @Test
    public void notTrace() {
        String returnValue = a.say("b");
        Assert.assertEquals(returnValue, "b");
        Assert.assertNull(subscriber.getEvent());
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
    public static class MockConditionHandler implements ConditionHandler {

        @Override
        public boolean test(AuditLogContext context) {
            String name = Convert.toStr(ArrayUtil.firstNonNull(context.getMethodArgs()));
            return "a".equals(name);
        }

        @Override
        public String id() {
            return "test";
        }
    }

    @Component
    public static class A {

        @TraceAuditLog(module = "M", action = "A", referenceId = "name", conditionId = "test")
        public String say(String name) {
            return name;
        }
    }

    @Component
    public static class AuditLogCreatedEventSubscriber extends AbstractMessageSubscriber<AuditLogCreatedEvent> {
        @Getter
        @Setter
        public AuditLogCreatedEvent event;

        @Override
        protected void internalOnMessage(AuditLogCreatedEvent auditLogCreatedEvent) {
            this.event = auditLogCreatedEvent;
        }
    }

    @EnableAspectJAutoProxy
    @Configuration
    @Import({BaseTestBeanConfig.class, SpringInitializedPublisher.class})
    @ComponentScan("org.team4u.base.log.audit.infrastructure.spring")
    public static class MockBeanConfig {

        @Bean
        public AuditLogAspect auditTraceAspect() {
            return new AuditLogAspect(new AuditLogAppService());
        }
    }
}