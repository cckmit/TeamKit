package org.team4u.selector.domain.selector.entity.whitelist;

import cn.hutool.core.collection.CollUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;
import org.team4u.selector.domain.selector.entity.binding.SimpleMapBinding;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 白名单选择器
 *
 * @author jay.wu
 */
public class WhitelistSelector implements Selector {

    public final static String MATCH = "MATCH";
    public final static String ANY = "*";

    private final Config config;

    public WhitelistSelector(Config config) {
        this.config = config;
    }

    /**
     * 选择
     *
     * @return 若命中则返回常量MATCH，负责为常量NONE
     */
    @Override
    public String select(SelectorBinding binding) {
        Map.Entry<String, Object> value = value(binding);

        if (keyOf(value.getKey(), value.getValue())) {
            return MATCH;
        }

        return NONE;
    }

    private boolean keyOf(String key, Object userId) {
        return config.getRules()
                .stream()
                .filter(it -> it.containsKey(key))
                .map(it -> matchName(key, it.get(key), userId))
                .findFirst()
                // 无法匹配key，则尝试查找*
                .orElseGet(() -> config.getRules()
                        .stream()
                        .filter(it -> it.containsKey(ANY))
                        .findFirst()
                        .map(it -> matchName(key, it.get(ANY), userId))
                        .orElse(false)
                );
    }

    private boolean matchName(String key, String nameId, Object userId) {
        if (key == null) {
            return false;
        }

        List<Object> names = namesOf(nameId);
        return CollUtil.contains(names, userId) || CollUtil.contains(names, ANY);
    }

    private List<Object> namesOf(String nameId) {
        if (config.names == null) {
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


    public static class Config {
        private Map<String, List<Object>> names;
        private List<Map<String, String>> rules;

        public Map<String, List<Object>> getNames() {
            return names;
        }

        public void setNames(Map<String, List<Object>> names) {
            this.names = names;
        }

        public List<Map<String, String>> getRules() {
            return rules;
        }

        public void setRules(List<Map<String, String>> rules) {
            this.rules = rules;
        }
    }
}