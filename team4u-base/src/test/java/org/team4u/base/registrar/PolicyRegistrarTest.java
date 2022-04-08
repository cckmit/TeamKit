package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.base.TestUtil;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.jvm.MessagePublisher;

import java.util.List;
import java.util.stream.Collectors;

public class PolicyRegistrarTest {

    private final TestPolicyRegistrar registrar = new TestPolicyRegistrar();

    @Before
    @After
    public void setUp() {
        registrar.unregisterAllPolicies();
        BeanProviders.getInstance().local().unregisterAllBeans();
        MessagePublisher.instance().reset();
    }

    @Test
    public void keyPolicy() {
        registrar.registerPoliciesBySuper(this.getClass().getPackage().getName(), StringIdPolicy.class);

        Assert.assertEquals(TestIdPolicy.class, registrar.policyOf(TestUtil.TEST3).getClass());
        Assert.assertEquals(Test2IdPolicy.class, registrar.policyOf(TestUtil.TEST2).getClass());

        Assert.assertEquals(
                CollUtil.newArrayList(TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policiesOf(TestUtil.TEST3).stream().map(StringIdPolicy::getClass).collect(Collectors.toList())
        );
        Assert.assertEquals(CollUtil.newArrayList(Test2IdPolicy.class, TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policies().stream().map(StringIdPolicy::getClass).collect(Collectors.toList()));
    }

    @Test
    public void noSuchPolicy() {
        try {
            registrar.availablePolicyOf(TestUtil.TEST3);
            Assert.fail();
        } catch (NoSuchPolicyException e) {
            Assert.assertEquals("Unable to find policy|context=" + TestUtil.TEST3, e.getMessage());
        }
    }

    @Test
    public void unregister() {
        TestIdPolicy policy = new TestIdPolicy();

        registrar.registerPolicy(policy);
        Assert.assertNotNull(registrar.policyOf(policy.id()));

        registrar.unregisterPolicy(policy);
        Assert.assertNull(registrar.policyOf(policy.id()));
    }

    @Test
    public void registerByBeanInitializedEvent() {
        TestIdPolicy policy = new TestIdPolicy();

        registrar.registerPoliciesByBeanInitializedEvent();
        BeanProviders.getInstance().local().registerBean(policy);

        Assert.assertEquals(policy, registrar.policyOf(policy.id()));
    }

    @Test
    public void registerByBeanProviders() {
        TestIdPolicy policy = new TestIdPolicy();
        BeanProviders.getInstance().local().registerBean(policy);

        registrar.registerPoliciesByBeanProviders(BeanProviders.getInstance());

        Assert.assertEquals(policy, registrar.policyOf(policy.id()));
    }

    private static class TestPolicyRegistrar extends PolicyRegistrar<String, TestId> {

        public TestPolicyRegistrar() {
        }

        public TestPolicyRegistrar(List<? extends TestId> policies) {
            super(policies);
        }
    }

    private interface TestId extends StringIdPolicy{

    }

    private static class TestIdPolicy implements TestId {

        @Override
        public String id() {
            return TestUtil.TEST3;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    private static class Test1IdPolicy implements TestId {

        @Override
        public String id() {
            return TestUtil.TEST3;
        }

        @Override
        public int priority() {
            return 20;
        }
    }

    private static class Test2IdPolicy implements TestId {

        @Override
        public String id() {
            return TestUtil.TEST2;
        }

        @Override
        public int priority() {
            return HIGHEST_PRECEDENCE;
        }
    }
}