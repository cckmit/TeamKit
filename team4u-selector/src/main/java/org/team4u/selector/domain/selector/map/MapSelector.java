package org.team4u.selector.domain.selector.map;

import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.Map;

/**
 * 映射选择器
 *
 * @author jay.wu
 */
public class MapSelector implements Selector {

    private final Map<String, String> config;

    public MapSelector(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String select(SelectorBinding binding) {
        String key = ((SingleValueBinding) binding).value();
        return config.getOrDefault(key, NONE);
    }
}