package org.team4u.selector.domain.selector.entity.mapping;

import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;
import org.team4u.selector.domain.selector.entity.binding.SingleValueBinding;

import java.util.Map;

/**
 * 映射选择器
 *
 * @author jay.wu
 */
public class MapSelector implements Selector {

    private final Map<String, String> rules;

    public MapSelector(Map<String, String> rules) {
        this.rules = rules;
    }

    @Override
    public String select(SelectorBinding binding) {
        String key = ((SingleValueBinding) binding).value();
        return rules.getOrDefault(key, NONE);
    }
}