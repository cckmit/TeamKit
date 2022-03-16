package org.team4u.base.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Map扩展工具类
 *
 * @author jay.wu
 */
public class MapExtUtil {

    /**
     * 将来源Map转换为目标类型的Map
     *
     * @param source           来源Map
     * @param targetKeyClass   目标Map的key类型
     * @param targetValueClass 目标Map的value类型
     * @param <K>              目标Map的key类型
     * @param <V>目标Map的value类型
     * @return 目标Map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> convert(Map<?, ?> source, Class<K> targetKeyClass, Class<V> targetValueClass) {
        return source.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        it -> Convert.convert(targetKeyClass, it.getKey()),
                        it -> Convert.convert(targetValueClass, it.getValue()),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        },
                        () -> ReflectUtil.newInstance(source.getClass())
                ));
    }
}