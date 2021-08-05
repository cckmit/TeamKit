package org.team4u.selector.domain.selector.map;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

/**
 * 动态映射选择器构建工厂
 *
 * @author jay.wu
 */
public class DynamicMapSelectorFactory extends AbstractSelectorFactoryFactory {

    public DynamicMapSelectorFactory() {
        this(CacheUtil.newLRUCache(1000));
    }

    public DynamicMapSelectorFactory(Cache<String, Selector> cache) {
        super(cache);
    }

    @Override
    public Selector call(String jsonConfig) {
        return new DynamicMapSelector(toConfig(jsonConfig));
    }

    public static DynamicMapSelector.Config toConfig(String jsonConfig) {
        return JSONUtil.toBean(
                jsonConfig,
                DynamicMapSelector.Config.class);
    }

    @Override
    public String id() {
        return "dynamicMap";
    }
}