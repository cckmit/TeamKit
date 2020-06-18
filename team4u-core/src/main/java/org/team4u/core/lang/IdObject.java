package org.team4u.core.lang;

/**
 * 声明该类型具有标识
 *
 * @param <V> 标识类型
 * @author jay.wu
 */
public interface IdObject<V> {

    /**
     * 获取标识
     */
    V id();
}