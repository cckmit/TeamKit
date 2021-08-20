package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.List;
import java.util.stream.Collectors;

public class PolicyRegistrarTest {

    private final TestPolicyRegistrar registrar = new TestPolicyRegistrar();

    @Test
    public void keyPolicy() {
        registrar.registerBySuper(this.getClass().getPackage().getName(), StringIdPolicy.class);

        Assert.assertEquals(TestIdPolicy.class, registrar.policyOf(TestUtil.TEST).getClass());
        Assert.assertEquals(Test2IdPolicy.class, registrar.policyOf(TestUtil.TEST2).getClass());

        Assert.assertEquals(
                CollUtil.newArrayList(TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policiesOf(TestUtil.TEST).stream().map(StringIdPolicy::getClass).collect(Collectors.toList())
        );
        Assert.assertEquals(CollUtil.newArrayList(Test2IdPolicy.class, TestIdPolicy.class, Test1IdPolicy.class),
                registrar.policies().stream().map(StringIdPolicy::getClass).collect(Collectors.toList()));
    }

    @Test
    public void availablePolicyOf() {
        try {
            registrar.availablePolicyOf(TestUtil.TEST);
            Assert.fail();
        } catch (NoSuchPolicyException e) {
            Assert.assertEquals("Unable to find policy|context=" + TestUtil.TEST, e.getMessage());
        }
    }

    @Test
    public void unregister() {
        TestIdPolicy policy = new TestIdPolicy();

        registrar.register(policy);
        Assert.assertNotNull(registrar.policyOf(policy.id()));

        registrar.unregister(policy);
        Assert.assertNull(registrar.policyOf(policy.id()));
    }

    private static class TestPolicyRegistrar extends PolicyRegistrar<String, StringIdPolicy> {

        public TestPolicyRegistrar() {
        }

        public TestPolicyRegistrar(List<? extends StringIdPolicy> policies) {
            super(policies);
        }
    }

    private static class TestIdPolicy implements StringIdPolicy {

        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    private static class Test1IdPolicy implements StringIdPolicy {

        @Override
        public String id() {
            return TestUtil.TEST;
        }

        @Override
        public int priority() {
            return 20;
        }
    }

    private static class Test2IdPolicy implements StringIdPolicy {

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