package org.team4u.selector.domain.selector.map;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

import java.util.Map;

/**
 * 映射选择器构建工厂
 *
 * @author jay.wu
 */
public class MapSelectorFactory extends AbstractSelectorFactoryFactory<Map<String, String>> {

    @Override
    protected Selector createWithConfig(Map<String, String> config) {
        return new MapSelector(new MapSelector.Config(config));
    }

    @Override
    public String id() {
        return "map";
    }
}