package org.team4u.selector.domain.selector.weight;

import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.util.ConfigUtil;

import java.util.LinkedHashMap;

/**
 * 权重选择器构建工厂
 *
 * @author jay.wu
 */
public class WeightSelectorFactory extends AbstractSelectorFactoryFactory<LinkedHashMap<String, Double>> {

    @Override
    public LinkedHashMap<String, Double> toConfig(String jsonConfig) {
        // 兼容旧配置
        if (ConfigUtil.isArrayConfig(jsonConfig)) {
            return ConfigUtil.parseMapListConfig(jsonConfig);
        }

        return super.toConfig(jsonConfig);
    }

    @Override
    public String id() {
        return "weight";
    }

    @Override
    protected Selector createWithConfig(LinkedHashMap<String, Double> config) {
        return new WeightSelector(config);
    }
}