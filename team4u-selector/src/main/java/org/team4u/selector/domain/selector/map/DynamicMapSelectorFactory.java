package org.team4u.selector.domain.selector.map;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

/**
 * 动态映射选择器构建工厂
 *
 * @author jay.wu
 */
public class DynamicMapSelectorFactory extends AbstractSelectorFactoryFactory<DynamicMapSelector.Config> {

    @Override
    public String id() {
        return "dynamicMap";
    }

    @Override
    protected Selector createWithConfig(DynamicMapSelector.Config config) {
        return new DynamicMapSelector(config);
    }
}