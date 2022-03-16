package org.team4u.selector.domain.selector.map;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import org.team4u.base.error.DataNotExistException;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorValueHandler;
import org.team4u.selector.domain.selector.SelectorValueService;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动态映射选择器
 *
 * @author jay.wu
 */
public class DynamicMapSelector implements Selector {

    private final Config config;

    private final MapSelector mapSelector;

    public DynamicMapSelector(Config config) {
        this.config = config;
        mapSelector = new MapSelector(new MapSelector.Config(config.getRules()));
    }

    @Override
    public String select(SelectorBinding binding) {
        Binding mapBinding = ((Binding) binding);

        String handlerId = mapSelector.select(new SingleValueBinding(mapBinding.getKey()));
        if (isNotMatch(handlerId)) {
            return NONE;
        }

        return select(mapBinding, handlerId);
    }

    private String select(Binding binding, String key) {
        Dict params = config.getHandlers().get(key);
        if (MapUtil.isEmpty(params)) {
            throw new DataNotExistException("Unable to find SelectorValueHandler configuration|key=" + key);
        }

        SelectorValueHandler.Context context = new SelectorValueHandler.Context(binding, params);
        SelectorValueHandler handler = binding.getValueService().availablePolicyOf(context.getId());
        return handler.handle(context);
    }

    @Data
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
            this.valueService = valueService == null ? new SelectorValueService() : valueService;
        }
    }

    @Data
    public static class Config {
        private Map<String, String> rules = new LinkedHashMap<>();
        private Map<String, Dict> handlers;
    }
}