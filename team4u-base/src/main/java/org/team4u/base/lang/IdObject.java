package org.team4u.base.lang;

/**
 * 声明该类型具有标识
 *
 * @param <V> 标识类型
 * @author jay.wu
 * @see org.team4u.base.registrar.IdPolicy
 * @deprecated 已废弃，使用IdPolicy代替
 */
public interface IdObject<V> {

    /**
     * 获取标识
     */
    V id();

    /**
     * 优先级
     * <p>
     * 数值越小，优先级越高
     */
    default int priority() {
        return 100;
    }
}