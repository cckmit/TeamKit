package org.team4u.selector.domain.selector.weight;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权重选择器
 *
 * @author jay.wu
 */
public class WeightSelector implements Selector {

    private final WeightRandom<String> weightRandom;

    /**
     * @param config 结果/权重映射集合
     */
    public WeightSelector(Map<String, Double> config) {
        this.weightRandom = buildWeightRandom(config);
    }

    private WeightRandom<String> buildWeightRandom(Map<String, Double> rules) {
        return RandomUtil.weightRandom(
                rules.entrySet()
                        .stream()
                        .map(it -> new WeightRandom.WeightObj<>(it.getKey(), it.getValue()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public SelectorResult select(SelectorBinding binding) {
        return SelectorResult.valueOf(weightRandom.next());
    }
}