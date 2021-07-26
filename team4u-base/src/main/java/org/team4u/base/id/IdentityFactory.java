package org.team4u.base.id;

import cn.hutool.core.lang.generator.Generator;

/**
 * 主键生成器
 *
 * @param <V> 主键类型
 * @author jay.wu
 */
public interface IdentityFactory<V> extends Generator<V> {

    /**
     * 生成主键
     *
     * @return 主键
     */
    V create();

    @Override
    default V next() {
        return create();
    }
}