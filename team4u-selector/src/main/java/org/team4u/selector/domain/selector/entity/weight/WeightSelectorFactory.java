package org.team4u.selector.domain.selector.entity.weight;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.SelectorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 权重选择器构建工厂
 *
 * @author jay.wu
 */
public class WeightSelectorFactory implements SelectorFactory {

    @Override
    public Selector create(String jsonConfig) {
        JSONArray config = JSONUtil.parseArray(jsonConfig);
        Map<String, Double> weightObjs = new HashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                weightObjs.put(w.getKey(), Convert.toDouble(w.getValue()));
            }
        }

        return new WeightSelector(weightObjs);
    }

    @Override
    public String id() {
        return "weight";
    }
}