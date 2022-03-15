package org.team4u.selector.domain.selector.whitelist;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public WhitelistSelector.Config toConfig(String configValue) {
        return Optional.ofNullable(parseOldConfig(configValue))
                .orElse(super.toConfig(configValue));
    }

    private WhitelistSelector.Config parseOldConfig(String configValue) {
        OldConfig oldConfig = JSONUtil.toBean(configValue, OldConfig.class);

        if (CollUtil.isEmpty(oldConfig.getRules())) {
            return null;
        }

        WhitelistSelector.Config newConfig = new WhitelistSelector.Config();
        newConfig.setNames(oldConfig.getNames());
        newConfig.setRules(CollUtil.getFirst(oldConfig.getRules()));
        return newConfig;
    }

    @Override
    protected Selector createWithConfig(WhitelistSelector.Config config) {
        return new WhitelistSelector(config);
    }

    @Data
    public static class OldConfig {
        private Map<String, List<Object>> names;
        private List<Map<String, String>> rules;
    }
}