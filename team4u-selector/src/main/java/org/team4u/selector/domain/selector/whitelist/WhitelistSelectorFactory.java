package org.team4u.selector.domain.selector.whitelist;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

/**
 * 白名单选择器构建工厂
 *
 * @author jay.wu
 */
public class WhitelistSelectorFactory extends AbstractSelectorFactoryFactory<WhitelistSelector.Config> {

    @Override
    public String id() {
        return "whitelist";
    }

    @Override
    protected Selector createWithConfig(WhitelistSelector.Config config) {
        return new WhitelistSelector(config);
    }
}