package org.team4u.selector.domain.selector;

import cn.hutool.core.util.StrUtil;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

/**
 * 选择执行器
 *
 * @author jay.wu
 */
public interface Selector {

    /**
     * 任意值
     */
    String ANY = "*";

    /**
     * 未命中规则常量值
     */
    String NONE = "NONE";

    /**
     * 命中规则常量值
     */
    String MATCH = "MATCH";

    /**
     * 根据绑定值选择最终结果
     *
     * @return 返回命中值，若无法命中，则返回常量NONE
     */
    String select(SelectorBinding binding);

    /**
     * 是否为未中规则
     */
    default boolean isNotMatch(String value) {
        return StrUtil.equals(value, NONE);
    }
}