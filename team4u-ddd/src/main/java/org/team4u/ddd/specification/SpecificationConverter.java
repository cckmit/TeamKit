package org.team4u.ddd.specification;

/**
 * @author jay.wu
 */
public interface SpecificationConverter<S extends Specification<T>, T, C, V> {

    V convert(S specification, C context);
}