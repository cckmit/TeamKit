package org.team4u.kit.core.lb;

import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.action.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权重轮询调度调度策略
 *
 * @param <T> 资源类型
 */
public class WeightRoundRobinPolicy<T> extends RoundRobinPolicy<T> {


    public WeightRoundRobinPolicy() {
    }

    /**
     * @param resourceWeights 资源集合
     */
    public WeightRoundRobinPolicy(Map<T, Integer> resourceWeights) {
        this.resources = initResources(resourceWeights);
    }

    public <V> V request(Map<T, Integer> resourceWeights, Function<T, V> action) {
        return super.request(initResources(resourceWeights), action);
    }

    public void request(Map<T, Integer> resourceWeights, Callback<T> action) {
        super.request(initResources(resourceWeights), action);
    }

    private List<T> initResources(Map<T, Integer> resourceWeights) {
        List<T> result = new ArrayList<T>();
        for (Map.Entry<T, Integer> entry : resourceWeights.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                result.add(entry.getKey());
            }
        }

        return result;
    }
}