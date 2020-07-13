package org.team4u.selector.domain.selector.entity;


import org.team4u.base.lang.IdObject;

/**
 * 选择执行器构建工厂
 *
 * @author jay.wu
 */
public interface SelectorFactory extends IdObject<String> {

    /**
     * 根据特定配置创建选择器
     */
    Selector create(String config);
}