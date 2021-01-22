package org.team4u.selector.domain.selector.weight;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.binding.ListBinding;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权重选择器
 *
 * @author jay.wu
 */
public class WeightSelector implements Selector {

    private final Map<String, Double> valueWeights;

    /**
     * @param valueWeights 结果/权重映射集合
     */
    public WeightSelector(Map<String, Double> valueWeights) {
        this.valueWeights = valueWeights;
    }

    @Override
    public String select(SelectorBinding binding) {
        if (MapUtil.isEmpty(valueWeights)) {
            return NONE;
        }

        if (binding == null) {
            return NONE;
        }

        Collection<String> values = values(binding);
        if (values.isEmpty()) {
            return NONE;
        }

        List<WeightRandom.WeightObj<String>> weightObjs = values(binding)
                .stream()
                .map(it -> new WeightRandom.WeightObj<>(it, this.valueWeights.get(it)))
                .collect(Collectors.toList());

        return ObjectUtil.defaultIfNull(RandomUtil.weightRandom(weightObjs).next(), NONE);
    }

    private Collection<String> values(SelectorBinding binding) {
        Collection<String> values = new ArrayList<>();

        if (binding instanceof SingleValueBinding) {
            values.add(((SingleValueBinding) binding).value());
            return values;
        }

        if (binding instanceof ListBinding) {
            return ((ListBinding) binding)
                    .stream()
                    .map(Convert::toStr)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}