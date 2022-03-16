package org.team4u.selector.domain.selector.whitelist;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;
import org.team4u.selector.domain.selector.map.MapSelector;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 白名单选择器
 *
 * @author jay.wu
 */
public class WhitelistSelector implements Selector {

    private final Config config;
    private final MapSelector mapSelector;

    public WhitelistSelector(Config config) {
        this.config = config;
        mapSelector = new MapSelector(new MapSelector.Config(config.getRules()));
    }

    /**
     * 选择
     *
     * @return 若命中则返回常量MATCH，否则为NONE
     */
    @Override
    public SelectorResult select(SelectorBinding binding) {
        Map.Entry<String, Object> value = value(binding);
        SelectorResult nameIdResult = mapSelector.select(new SingleValueBinding(value.getKey()));

        if (!nameIdResult.isMatch()) {
            return SelectorResult.NOT_MATCH;
        }

        return SelectorResult.valueOf(matchName(nameIdResult.toString(), value.getValue()));
    }

    private boolean matchName(String nameId, Object userId) {
        List<Object> names = namesOf(nameId);
        return CollUtil.contains(names, userId) || CollUtil.contains(names, ANY);
    }

    private List<Object> namesOf(String nameId) {
        if (config.getNames() == null) {
            return Collections.emptyList();
        }

        return config.getNames().getOrDefault(nameId, Collections.emptyList());
    }

    private Map.Entry<String, Object> value(SelectorBinding binding) {
        if (binding instanceof SimpleMapBinding) {
            return CollUtil.getFirst(((SimpleMapBinding) binding).entrySet());
        }

        return null;
    }

    @Data
    public static class Config {
        private Map<String, List<Object>> names;
        private Map<String, String> rules = new LinkedHashMap<>();
    }
}