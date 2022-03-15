package org.team4u.selector.domain.selector.probability;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.util.ConfigUtil;

import java.util.Map;

/**
 * 概率选择器构建工厂
 *
 * @author jay.wu
 */
public class ProbabilitySelectorFactory extends AbstractSelectorFactoryFactory<Map<String, Double>> {

    @Override
    public Map<String, Double> toConfig(String jsonConfig) {
        // 兼容旧配置
        if (ConfigUtil.isArrayConfig(jsonConfig)) {
            return ConfigUtil.parseMapListConfig(jsonConfig);
        }

        return super.toConfig(jsonConfig);
    }

    @Override
    public String id() {
        return "probability";
    }

    @Override
    protected Selector createWithConfig(Map<String, Double> config) {
        return new ProbabilitySelector(config);
    }
}