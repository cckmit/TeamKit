package org.team4u.ddd.specification;

/**
 * 非规格
 */
class NotSpecification<C> extends AbstractSpecification<C> {

    private Specification<C> wrapped;

    public NotSpecification(Specification<C> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean isSatisfiedBy(C context) {
        return !wrapped.isSatisfiedBy(context);
    }
}