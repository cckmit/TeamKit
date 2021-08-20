package org.team4u.selector.domain.selector.map;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorValueHandler;
import org.team4u.selector.domain.selector.SelectorValueService;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

import java.util.Map;

/**
 * 动态映射选择器
 *
 * @author jay.wu
 */
public class DynamicMapSelector implements Selector {

    private static final String EMPTY_RULE = "EMPTY_RULE";

    private final Config config;

    public DynamicMapSelector(Config config) {
        this.config = config;
    }

    @Override
    public String select(SelectorBinding binding) {
        Binding mapBinding = ((Binding) binding);
        if (mapBinding.getValueService() == null) {
            return NONE;
        }

        // 尝试匹配精确key
        String result = select(mapBinding, mapBinding.getKey());
        if (!StrUtil.equals(result, EMPTY_RULE)) {
            return result;
        }

        // 尝试匹配*
        result = select(mapBinding, ANY);
        if (StrUtil.equals(result, EMPTY_RULE)) {
            // 没有匹配任何key
            return NONE;
        }

        return result;
    }

    private String select(Binding binding, String key) {
        String ruleId = config.getRules().get(key);
        if (StrUtil.isBlank(ruleId)) {
            return EMPTY_RULE;
        }

        Dict params = config.getHandlers().get(ruleId);
        if (MapUtil.isEmpty(params)) {
            return EMPTY_RULE;
        }

        SelectorValueHandler.Context context = new SelectorValueHandler.Context(binding, params);
        SelectorValueHandler handler = binding.getValueService().policyOf(context.getId());
        if (handler == null) {
            return NONE;
        }
        return handler.handle(context);
    }

    public static class Binding implements SelectorBinding {
        private final String key;
        private final Dict extend;
        private final SelectorValueService valueService;

        public Binding(String key, SelectorValueService valueService) {
            this(key, null, valueService);
        }

        public Binding(String key, Dict extend, SelectorValueService valueService) {
            this.key = key;
            this.extend = extend;
            this.valueService = valueService;
        }

        public Dict getExtend() {
            return extend;
        }

        public String getKey() {
            return key;
        }

        public SelectorValueService getValueService() {
            return valueService;
        }
    }

    public static class Config {
        private Map<String, String> rules;
        private Map<String, Dict> handlers;

        public Map<String, String> getRules() {
            return rules;
        }

        public Config setRules(Map<String, String> rules) {
            this.rules = rules;
            return this;
        }

        public Map<String, Dict> getHandlers() {
            return handlers;
        }

        public Config setHandlers(Map<String, Dict> handlers) {
            this.handlers = handlers;
            return this;
        }
    }
}