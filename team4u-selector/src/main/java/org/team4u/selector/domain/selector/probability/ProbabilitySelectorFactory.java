package org.team4u.selector.domain.selector.probability;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
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
public class ProbabilitySelectorFactory extends AbstractSelectorFactoryFactory {

    public ProbabilitySelectorFactory() {
        this(CacheUtil.newLRUCache(1000));
    }

    public ProbabilitySelectorFactory(Cache<String, Selector> cache) {
        super(cache);
    }

    @Override
    public Selector call(String jsonConfig) {
        JSONArray config = JSONUtil.parseArray(jsonConfig);
        Map<String, Double> weightObjs = new HashMap<>();

        for (Object o : config) {
            JSONObject weightObj = (JSONObject) o;
            for (Map.Entry<String, Object> w : weightObj.entrySet()) {
                weightObjs.put(w.getKey(), Convert.toDouble(w.getValue()));
            }
        }

        return new ProbabilitySelector(weightObjs);
    }

    @Override
    public String id() {
        return "probability";
    }
}