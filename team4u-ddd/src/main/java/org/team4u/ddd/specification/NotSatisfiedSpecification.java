package org.team4u.ddd.specification;


/**
 * 不合适规格
 *
 * @author jay.wu
 */
public class NotSatisfiedSpecification<C> extends AbstractSpecification<C> {

    @Override
    public boolean isSatisfiedBy(C context) {
        return false;
    }
}