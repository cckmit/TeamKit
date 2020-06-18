package org.team4u.core.id;

/**
 * 主键生成器
 *
 * @param <V> 主键类型
 * @author jay.wu
 */
public interface IdentityFactory<V> {

    /**
     * 生成主键
     *
     * @return 主键
     */
    V create();
}