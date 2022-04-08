package org.team4u.kv;

import org.team4u.base.serializer.Serializer;
import org.team4u.base.serializer.SmartSerializer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * 简单键值应用服务
 * <p>
 * 相比KeyValueService，本类针对同一种类型/过期时长提供更加便捷的接口
 * <p>
 * 支持大部分Map特性，可持久化到特定的资源库
 *
 * @param <V> 值对象类型
 */
public class SimpleKeyValueService<V> {

    /**
     * 有效期（毫秒），0为永不过期
     */
    private final int ttlMillis;
    /**
     * 类型
     */
    private final String type;
    /**
     * 值类型
     */
    private final Class<V> valueClass;

    private final KeyValueService keyValueService;

    public SimpleKeyValueService(String type,
                                 Class<V> valueClass,
                                 KeyValueRepository keyValueRepository) {
        this(type, 0, valueClass, SmartSerializer.getInstance(), keyValueRepository, null);
    }


    public SimpleKeyValueService(String type,
                                 int ttlMillis,
                                 Class<V> valueClass,
                                 KeyValueRepository keyValueRepository,
                                 KeyValueCleaner keyValueCleaner) {
        this(type, ttlMillis, valueClass, SmartSerializer.getInstance(), keyValueRepository, keyValueCleaner);
    }

    public SimpleKeyValueService(String type,
                                 int ttlMillis,
                                 Class<V> valueClass,
                                 Serializer valueSerializer,
                                 KeyValueRepository keyValueRepository,
                                 KeyValueCleaner keyValueCleaner) {
        this.type = type;
        this.ttlMillis = ttlMillis;
        this.valueClass = valueClass;
        this.keyValueService = new KeyValueService(valueSerializer, keyValueRepository, keyValueCleaner);
    }

    /**
     * 获取值
     */
    public V get(String key) {
        return keyValueService.get(type(), key, valueClass());
    }

    /**
     * 获取值，直到超时
     *
     * @param key              键
     * @param retryDelayMillis 重试间隔（毫秒）
     * @param timeoutMillis    最大重试时间（毫秒）
     */
    public V get(String key, int retryDelayMillis, int timeoutMillis) throws TimeoutException {
        return keyValueService.get(type(), key, valueClass(), retryDelayMillis, timeoutMillis);
    }

    /**
     * 设置值
     */
    public String put(String key, V value) {
        return keyValueService.put(type(), key, value, ttlMillis());
    }

    /**
     * 仅在键不存在时能够成功设置值
     *
     * @return 若成功设置则返回key
     */
    public String putIfAbsent(String key, V value) {
        return keyValueService.putIfAbsent(type(), key, value, ttlMillis());
    }

    /**
     * 删除指定键值
     */
    public void remove(String key) {
        keyValueService.remove(type(), key);
    }

    /**
     * 批量设置键值
     */
    public void putAll(Map<? extends String, ? extends V> m) {
        keyValueService.putAll(type(), m);
    }

    /**
     * 检查是否包含指定key
     */
    public boolean containsKey(String key) {
        return keyValueService.containsKey(type(), key);
    }

    /**
     * 获取所有值集合
     */
    public Collection<V> values() {
        return keyValueService.values(type(), valueClass());
    }

    /**
     * 获取所有键值集合
     */
    public Set<KeyValueService.KeyValueEntry<V>> entrySet() {
        return keyValueService.entrySet(type(), valueClass());
    }

    /**
     * 获取键值数量
     */
    public long size() {
        return keyValueService.size(type());
    }

    /**
     * 清理键值
     */
    public void clear() {
        keyValueService.clear(type());
    }

    /**
     * 获取键集合
     */
    public Set<String> keySet() {
        return keyValueService.keySet(type());
    }

    /**
     * 清理过期对象
     * <p>
     * 若待删除数量过多，将循环分批删除
     */
    public void removeExpirationValues(int batchSize) {
        keyValueService.removeExpirationValues(batchSize);
    }

    /**
     * 返回指定键值的过期时间（毫秒）
     *
     * @return -1为key不存在，0为key永不过期，其余为过期的标准时间戳
     */
    public long expirationTimestamp(String key) {
        return keyValueService.expirationTimestamp(type(), key);
    }

    /**
     * 当前键值是否为空
     */
    public boolean isEmpty() {
        return keyValueService.isEmpty(type());
    }

    /**
     * 获取有效期时长（秒）
     */
    public int ttlMillis() {
        return ttlMillis;
    }

    /**
     * 获取键值类型
     */
    public String type() {
        return type;
    }

    /**
     * 获取值转换器
     */
    public Serializer valueSerializer() {
        return keyValueService.valueSerializer();
    }

    /**
     * 获取值类型
     */
    public Class<V> valueClass() {
        return valueClass;
    }
}