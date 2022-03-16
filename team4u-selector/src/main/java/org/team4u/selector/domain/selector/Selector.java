package org.team4u.selector.domain.selector;

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
     * 根据绑定值选择最终结果
     */
    SelectorResult select(SelectorBinding binding);
}