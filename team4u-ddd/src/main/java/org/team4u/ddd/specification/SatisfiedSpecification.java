package org.team4u.ddd.specification;

/**
 * 合适规格
 *
 * @author jay.wu
 */
public class SatisfiedSpecification<C> extends AbstractSpecification<C> {

    @Override
    public boolean isSatisfiedBy(C context) {
        return true;
    }
}