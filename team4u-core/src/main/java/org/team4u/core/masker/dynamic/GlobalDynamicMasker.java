package org.team4u.core.masker.dynamic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.team4u.core.masker.Masker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局动态掩码器
 *
 * @author jay.wu
 */
public class GlobalDynamicMasker implements Masker {

    private final DynamicMaskerConfig config;

    public GlobalDynamicMasker(DynamicMaskerConfig config) {
        this.config = config;
    }

    @Override
    public String mask(Object value) {
        return JSON.toJSONString(value, new FastJsonValueFilter(nameMaskers()));
    }

    private Map<String, Masker> nameMaskers() {
        Map<String, Masker> nameMaskers = new HashMap<>();

        for (Map.Entry<Masker, List<String>> entry : config.getMaskerExpressions().entrySet()) {
            Masker masker = entry.getKey();
            List<String> names = entry.getValue();
            for (String name : names) {
                nameMaskers.put(name, masker);
            }
        }

        return nameMaskers;
    }

    private static class FastJsonValueFilter implements ValueFilter {

        private final Map<String, Masker> nameMaskers;

        public FastJsonValueFilter(Map<String, Masker> nameMaskers) {
            this.nameMaskers = nameMaskers;
        }

        @Override
        public Object process(Object object, String name, Object value) {
            if (value == null) {
                return null;
            }

            Masker masker = nameMaskers.get(name);
            if (masker == null) {
                return value;
            }

            return masker.mask(value.toString());
        }
    }
}