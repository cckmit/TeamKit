package org.team4u.selector.domain.selector;

/**
 * 选择器配置资源库
 *
 * @author jay.wu
 */
public interface SelectorConfigRepository {

    /**
     * 根据选择器标识获取选择器
     */
    SelectorConfig selectorConfigOfId(String id);
}