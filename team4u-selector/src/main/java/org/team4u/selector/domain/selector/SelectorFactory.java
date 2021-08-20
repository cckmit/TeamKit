package org.team4u.selector.domain.selector;


import org.team4u.base.registrar.IdPolicy;

/**
 * 选择执行器构建工厂
 *
 * @author jay.wu
 */
public interface SelectorFactory extends IdPolicy<String> {

    /**
     * 根据特定配置创建选择器
     */
    Selector create(String config);
}