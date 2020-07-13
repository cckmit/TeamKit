package org.team4u.ddd.specification;

/**
 * 或规格
 *
 * @author jay.wu
 */
class OrSpecification<C> extends AbstractSpecification<C> {

    private final Specification<C> one;
    private final Specification<C> other;

    public OrSpecification(Specification<C> one, Specification<C> other) {
        this.one = one;
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(C context) {
        return one.isSatisfiedBy(context) || other.isSatisfiedBy(context);
    }
}
