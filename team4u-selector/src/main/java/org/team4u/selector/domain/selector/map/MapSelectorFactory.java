package org.team4u.selector.domain.selector.map;

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
public class MapSelectorFactory extends AbstractSelectorFactoryFactory<Map<String, String>> {

    @Override
    public Map<String, String> toConfig(String jsonConfig) {
        JSONObject config = JSONUtil.parseObj(jsonConfig);
        Map<String, String> rules = new HashMap<>(config.size());

        for (Map.Entry<String, Object> entry : config.entrySet()) {
            rules.put(entry.getKey(), Convert.toStr(entry.getValue()));
        }

        return rules;
    }

    @Override
    protected Selector createWithConfig(Map<String, String> config) {
        return new MapSelector(config);
    }

    @Override
    public String id() {
        return "map";
    }
}