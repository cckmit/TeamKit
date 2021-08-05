package org.team4u.selector.domain.selector.map;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射选择器构建工厂
 *
 * @author jay.wu
 */
public class MapSelectorFactory extends AbstractSelectorFactoryFactory {

    public MapSelectorFactory() {
        this(CacheUtil.newLRUCache(1000));
    }

    public MapSelectorFactory(Cache<String, Selector> cache) {
        super(cache);
    }

    @Override
    public Selector call(String jsonConfig) {
        return new MapSelector(toConfig(jsonConfig));
    }

    public static Map<String, String> toConfig(String jsonConfig) {
        JSONObject config = JSONUtil.parseObj(jsonConfig);
        Map<String, String> rules = new HashMap<>(config.size());

        for (Map.Entry<String, Object> entry : config.entrySet()) {
            rules.put(entry.getKey(), Convert.toStr(entry.getValue()));
        }

        return rules;
    }

    @Override
    public String id() {
        return "map";
    }
}