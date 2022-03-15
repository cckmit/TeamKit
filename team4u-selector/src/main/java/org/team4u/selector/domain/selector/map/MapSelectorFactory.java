package org.team4u.selector.domain.selector.map;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

import java.util.LinkedHashMap;

/**
 * 映射选择器构建工厂
 *
 * @author jay.wu
 */
public class MapSelectorFactory extends AbstractSelectorFactoryFactory<LinkedHashMap<String, String>> {

    @Override
    protected Selector createWithConfig(LinkedHashMap<String, String> config) {
        return new MapSelector(new MapSelector.Config(config));
    }

    @Override
    public String id() {
        return "map";
    }
}