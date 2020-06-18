package org.team4u.ddd.specification;

import org.junit.Assert;
import org.junit.Test;

public class AbstractSpecificationTest {

    @Test
    public void or() {
        Specification<String> s1 = mockSpecification(true);
        Specification<String> s2 = mockSpecification(false);
        Assert.assertTrue(s1.or(s2).isSatisfiedBy(null));

        s1 = mockSpecification(false);
        s2 = mockSpecification(true);
        Assert.assertTrue(s1.or(s2).isSatisfiedBy(null));

        s1 = mockSpecification(false);
        s2 = mockSpecification(false);
        Assert.assertFalse(s1.or(s2).isSatisfiedBy(null));

        s1 = mockSpecification(true);
        s2 = mockSpecification(true);
        Assert.assertTrue(s1.or(s2).isSatisfiedBy(null));
    }

    @Test
    public void not() {
        Specification<String> s = mockSpecification(true);
        Assert.assertFalse(s.not().isSatisfiedBy(null));
    }

    @Test
    public void orNot() {
        Specification<String> s1 = mockSpecification(false);
        Specification<String> s2 = mockSpecification(false);
        Assert.assertTrue(s1.or(s2.not()).isSatisfiedBy(null));
    }

    @Test
    public void and() {
        Specification<String> s1 = mockSpecification(true);
        Specification<String> s2 = mockSpecification(true);
        Assert.assertTrue(s1.and(s2).isSatisfiedBy(null));
    }

    private Specification<String> mockSpecification(boolean isSatisfied) {
        return new AbstractSpecification<String>() {

            @Override
            public boolean isSatisfiedBy(String channelId) {
                return isSatisfied;
            }
        };
    }
}