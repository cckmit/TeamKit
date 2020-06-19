package org.team4u.kv.model;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 键值工厂
 * <p>
 * 专门负责构建键值对象
 *
 * @author jay.wu
 */
public class KeyValueFactory {

    public static KeyValue create(String type,
                                  String name,
                                  String value,
                                  int ttlMillis) {
        return create(type, name, value, ttlMillis == 0 ? 0 : DateUtil.offsetMillisecond(new Date(), ttlMillis).getTime());
    }

    public static KeyValue create(String type,
                                  String name,
                                  String value,
                                  long expirationTimestamp) {
        return new KeyValue()
                .setId(KeyValueId.of(type, name))
                .setExpirationTimestamp(expirationTimestamp)
                .setValue(value);
    }
}