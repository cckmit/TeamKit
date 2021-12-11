package org.team4u.selector.domain.selector.probability;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.AbstractSelectorFactoryFactory;
import org.team4u.selector.domain.selector.Selector;

import java.util.HashMap;
import java.util.Map;

/**
 * 概率选择器构建工厂
 *
 * @author jay.wu
 */
public class ProbabilitySelectorFactory extends AbstractSelectorFactoryFactory<Map<String, Double>> {

    @Override
    public Map<String, Double> toConfig(String jsonConfig) {
        JSONArray config = JSONUtil.parseArray(jsonConfig);
        Map<String, Double> weightObjs = new HashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                weightObjs.put(w.getKey(), Convert.toDouble(w.getValue()));
            }
        }

        return weightObjs;
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