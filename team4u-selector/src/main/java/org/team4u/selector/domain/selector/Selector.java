package org.team4u.selector.domain.selector;

import org.team4u.selector.domain.selector.binding.SelectorBinding;

/**
 * 选择执行器
 *
 * @author jay.wu
 */
public interface Selector {

    String NONE = "NONE";

    /**
     * 根据绑定值选择最终结果
     *
     * @return 返回命中值，若无法命中，则返回常量NONE
     */
    String select(SelectorBinding binding);
}