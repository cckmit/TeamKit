package org.team4u.selector.domain.selector.whitelist;

import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorFactory;

/**
 * 白名单选择器构建工厂
 *
 * @author jay.wu
 */
public class WhitelistSelectorFactory implements SelectorFactory {

    @Override
    public Selector create(String jsonConfig) {
        return new WhitelistSelector(JSONUtil.toBean(jsonConfig, WhitelistSelector.Config.class));
    }

    @Override
    public String id() {
        return "whitelist";
    }
}