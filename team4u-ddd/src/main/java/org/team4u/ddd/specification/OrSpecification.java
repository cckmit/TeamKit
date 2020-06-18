package org.team4u.ddd.specification;

/**
 * 或规格
 */
class OrSpecification<C> extends AbstractSpecification<C> {

    private Specification<C> one;
    private Specification<C> other;

    public OrSpecification(Specification<C> one, Specification<C> other) {
        this.one = one;
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(C context) {
        return one.isSatisfiedBy(context) || other.isSatisfiedBy(context);
    }
}
