package org.team4u.selector.domain.selector;

import org.team4u.selector.domain.selector.binding.SelectorBinding;

/**
 * 选择执行器
 *
 * @author jay.wu
 */
public interface Selector {
    /**
     * 根据绑定值选择最终结果
     *
     * @param binding 绑定值
     * @return 选择器结果
     */
    SelectorResult select(SelectorBinding binding);
}