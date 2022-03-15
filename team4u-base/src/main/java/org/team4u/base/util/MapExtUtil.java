package org.team4u.base.util;

import cn.hutool.core.convert.Convert;

import java.util.Map;
import java.util.stream.Collectors;

public class MapExtUtil {

    public static <K, V> Map<K, V> convert(Map<?, ?> source, Class<K> keyClass, Class<V> valueClass) {
        return source.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        it -> Convert.convert(keyClass, it.getKey()),
                        it -> Convert.convert(valueClass, it.getValue())
                ));
    }
}