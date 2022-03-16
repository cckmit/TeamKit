package org.team4u.selector.domain.selector.probability;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import org.team4u.base.util.MapExtUtil;
import org.team4u.selector.domain.selector.Selector;
import org.team4u.selector.domain.selector.SelectorResult;
import org.team4u.selector.domain.selector.binding.ListBinding;
import org.team4u.selector.domain.selector.binding.SelectorBinding;
import org.team4u.selector.domain.selector.binding.SimpleMapBinding;
import org.team4u.selector.domain.selector.binding.SingleValueBinding;
import org.team4u.selector.domain.selector.map.MapSelector;

import java.util.Map;

/**
 * 概率选择执行器
 *
 * @author jay.wu
 */
public class ProbabilitySelector implements Selector {

    private final MapSelector mapSelector;

    /**
     * @param config 值/百分比概率映射集合
     *               1001 -> 90，表示90%的概率命中1001
     *               * -> 10，表示默认命中概率为10%
     */
    public ProbabilitySelector(Map<String, Double> config) {
        mapSelector = new MapSelector(new MapSelector.Config(
                MapExtUtil.convert(config, String.class, String.class)
        ));
    }

    /**
     * 选择
     *
     * @return 若命中则返回常量MATCH，否则为常量NONE
     */
    @Override
    public SelectorResult select(SelectorBinding binding) {
        String key = Convert.toStr(keyOf(binding));
        SelectorResult probabilityResult = mapSelector.select(new SingleValueBinding(key));

        if (!probabilityResult.isMatch()) {
            return SelectorResult.NOT_MATCH;
        }

        return SelectorResult.createWithMatch(isMatch(probabilityResult.convert(Double.class)));
    }

    private boolean isMatch(Double probability) {
        int target = RandomUtil.randomInt(1, 101);

        return probability >= target;
    }

    private Object keyOf(SelectorBinding binding) {
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