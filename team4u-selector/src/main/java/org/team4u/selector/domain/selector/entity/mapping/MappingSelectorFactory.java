package org.team4u.selector.domain.selector.entity.mapping;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.SelectorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 表达式选择器构建工厂
 *
 * @author jay.wu
 */
public class MappingSelectorFactory implements SelectorFactory {

    public static Map<String, String> toConfig(String jsonConfig) {
        JSONObject config = JSONUtil.parseObj(jsonConfig);
        Map<String, String> rules = new HashMap<>(config.size());

        for (Map.Entry<String, Object> entry : config.entrySet()) {
            rules.put(entry.getKey(), Convert.toStr(entry.getValue()));
        }

        return rules;
    }

    @Override
    public Selector create(String jsonConfig) {
        return new MappingSelector(toConfig(jsonConfig));
    }

    @Override
    public String id() {
        return "mapping";
    }
}