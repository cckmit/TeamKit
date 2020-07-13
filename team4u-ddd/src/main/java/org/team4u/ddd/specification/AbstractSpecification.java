package org.team4u.ddd.specification;

/**
 * 规格抽象类
 *
 * @author jay.wu
 */
public abstract class AbstractSpecification<C> implements Specification<C> {

    @Override
    public Specification<C> or(Specification<C> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public Specification<C> not() {
        return new NotSpecification<>(this);
    }

    @Override
    public Specification<C> and(Specification<C> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public Specification<C> andX(Specification<C> other) {
        return new AndXSpecification<>(this, other);
    }

    @Override
    public Specification<C> orX(Specification<C> other) {
        return new OrXSpecification<>(this, other);
    }
}