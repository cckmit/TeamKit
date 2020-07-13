package org.team4u.ddd.specification;

/**
 * 规格
 *
 * @param <C> 上下文
 * @author jay.wu
 */
public interface Specification<C> {
    /**
     * 是否满足规格
     */
    boolean isSatisfiedBy(C context);

    /**
     * 和其他规格与关系
     */
    Specification<C> and(Specification<C> other);

    /**
     * 特殊与运算
     * <p>
     * 先回依次执行两个规则，再判断是否满足条件
     */
    Specification<C> andX(Specification<C> other);

    /**
     * 和其他规格或关系
     */
    Specification<C> or(Specification<C> other);

    /**
     * 特殊或运算
     * <p>
     * 先回依次执行两个规则，再判断是否满足条件
     */
    Specification<C> orX(Specification<C> other);

    /**
     * 和现有规格非关系
     */
    Specification<C> not();
}