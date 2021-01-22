package org.team4u.selector.infrastructure.persistence;

import org.team4u.selector.domain.selector.SelectorConfig;
import org.team4u.selector.domain.selector.SelectorConfigRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于内存的选择器资源库
 *
 * @author jay.wu
 */
public class InMemorySelectorConfigRepository implements SelectorConfigRepository {

    private final Map<String, SelectorConfig> selectorMap = new HashMap<>();

    public InMemorySelectorConfigRepository(List<SelectorConfig> values) {
        for (SelectorConfig value : values) {
            selectorMap.put(value.getId(), value);
        }
    }

    @Override
    public SelectorConfig selectorConfigOfId(String id) {
        return selectorMap.get(id);
    }
}