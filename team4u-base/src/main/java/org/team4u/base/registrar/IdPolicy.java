package org.team4u.base.registrar;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

/**
 * 拥有标识的策略
 *
 * @param <ID> 标识类型
 * @author jay.wu
 */
public interface IdPolicy<ID> extends Policy<ID> {

    /**
     * 获取标识
     */
    ID id();

    @Override
    default boolean isSupport(ID context) {
        return ObjectUtil.equals(context, id());
    }

    @Override
    default String policyName() {
        return Convert.toStr(id());
    }
}