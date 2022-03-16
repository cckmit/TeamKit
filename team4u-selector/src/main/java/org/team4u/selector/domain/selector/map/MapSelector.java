package org.team4u.selector.domain.selector.map;

import cn.hutool.core.util.ReUtil;
import lombok.Data;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.Map;
import java.util.Optional;

/**
 * 映射选择器
 *
 * @author jay.wu
 */
public class MapSelector implements Selector {

    private final Config config;

    public MapSelector(Config config) {
        this.config = config;
    }

    @Override
    public SelectorResult select(SelectorBinding binding) {
        String key = ((SingleValueBinding) binding).value();
        // 优先匹配精确key，其次匹配符合正则表达式的key，若均无法找到则返回null
        return new SelectorResult(
                findByFullKey(key).orElseGet(
                        () -> findByRegexKey(key).orElse(null)
                )
        );
    }

    private Optional<String> findByFullKey(String key) {
        return Optional.ofNullable(config.getRules().get(key));
    }

    private Optional<String> findByRegexKey(String key) {
        return config.getRules().entrySet()
                .stream()
                .filter(it -> ReUtil.isMatch(it.getKey(), key))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Data
    public static class Config {
        private final Map<String, String> rules;

        public Config(Map<String, String> rules) {
            this.rules = rules;

            initRules();
        }

        private void initRules() {
            standardizedWildcard();
        }

        /**
         * 将通配符转换为正则表达式
         */
        private void standardizedWildcard() {
            String defaultValue = rules.get(ANY);
            if (defaultValue == null) {
                return;
            }

            rules.remove(ANY);
            rules.put("." + ANY, defaultValue);
        }
    }
}