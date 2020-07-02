package org.team4u.ddd.specification;

/**
 * 特殊与规格
 * <p>
 * 先回依次执行两个规则，再判断是否满足条件
 *
 * @author jay.wu
 */
class AndXSpecification<C> extends AbstractSpecification<C> {

    private final Specification<C> one;
    private final Specification<C> other;

    public AndXSpecification(Specification<C> one, Specification<C> other) {
        this.one = one;
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(C context) {
        boolean isSatisfiedByOne = one.isSatisfiedBy(context);
        boolean isSatisfiedByOther = other.isSatisfiedBy(context);
        return isSatisfiedByOne && isSatisfiedByOther;
    }
}