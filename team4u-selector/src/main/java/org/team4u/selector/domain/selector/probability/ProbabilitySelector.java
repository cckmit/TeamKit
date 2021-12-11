package org.team4u.selector.domain.selector.probability;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.binding.ListBinding;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.Map;

/**
 * 概率选择执行器
 *
 * @author jay.wu
 */
public class ProbabilitySelector implements Selector {

    private final Map<String, Double> config;

    /**
     * @param config 值/百分比概率映射集合
     *                           1001 -> 90，表示90%的概率命中1001
     *                           * -> 10，表示默认命中概率为10%
     */
    public ProbabilitySelector(Map<String, Double> config) {
        this.config = config;
    }

    /**
     * 选择
     *
     * @return 若命中则返回常量MATCH，否则为常量NONE
     */
    @Override
    public String select(SelectorBinding binding) {
        Object value = value(binding);

        int target = RandomUtil.randomInt(1, 101);
        Double probability = probabilityOfValue(value);

        if (probability >= target) {
            return MATCH;
        }

        return NONE;
    }

    private Double probabilityOfValue(Object value) {
        Double defaultProbability = ObjectUtil.defaultIfNull(config.get(ANY), 0.0);

        if (value == null) {
            return defaultProbability;
        }

        String stringValue = Convert.toStr(value);

        Double probability = config.get(stringValue);
        return ObjectUtil.defaultIfNull(probability, defaultProbability);
    }

    private Object value(SelectorBinding binding) {
        if (binding instanceof SingleValueBinding) {
            return ((SingleValueBinding) binding).value();
        }

        if (binding instanceof ListBinding) {
            return CollUtil.getFirst(((ListBinding) binding));
        }

        if (binding instanceof SimpleMapBinding) {
            return CollUtil.getFirst(((SimpleMapBinding) binding).values());
        }

        return null;
    }
}