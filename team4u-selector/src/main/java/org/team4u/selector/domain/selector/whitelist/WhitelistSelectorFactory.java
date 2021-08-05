package org.team4u.selector.domain.selector.whitelist;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

/**
 * 白名单选择器构建工厂
 *
 * @author jay.wu
 */
public class WhitelistSelectorFactory extends AbstractSelectorFactoryFactory {

    public WhitelistSelectorFactory(Cache<String, Selector> cache) {
        super(cache);
    }

    public WhitelistSelectorFactory() {
        this(CacheUtil.newLRUCache(1000));
    }

    @Override
    public String id() {
        return "whitelist";
    }

    @Override
    public Selector call(String jsonConfig) {
        return new WhitelistSelector(JSONUtil.toBean(jsonConfig, WhitelistSelector.Config.class));
    }
}