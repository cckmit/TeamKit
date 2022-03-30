package org.team4u.kv;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.serializer.JsonOrSimpleValueSerializer;
import org.team4u.base.serializer.Serializer;
import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueFactory;
import org.team4u.kv.model.KeyValueId;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 键值应用服务
 * <p>
 * 支持大部分Map特性，可持久化到特定的资源库
 */
public class KeyValueService {

    private final Log log = LogFactory.get();
    /**
     * 值转换器
     */
    private final Serializer valueSerializer;
    /**
     * 键值资源库
     */
    private final KeyValueRepository keyValueRepository;

    public KeyValueService(KeyValueRepository keyValueRepository) {
        this(keyValueRepository, null);
    }

    public KeyValueService(KeyValueRepository keyValueRepository,
                           KeyValueCleaner keyValueCleaner) {
        this(JsonOrSimpleValueSerializer.noCache(), keyValueRepository, keyValueCleaner);
    }

    public KeyValueService(Serializer valueSerializer,
                           KeyValueRepository keyValueRepository,
                           KeyValueCleaner keyValueCleaner) {
        this.valueSerializer = valueSerializer;
        this.keyValueRepository = keyValueRepository;

        if (keyValueCleaner != null) {
            keyValueCleaner.addKeyValueRepository(keyValueRepository);
        }
    }

    /**
     * 获取值
     */
    public <V> V get(String type, String key, Class<V> valueClass) {
        KeyValue kv = keyValueRepository().byId(KeyValueId.of(type, key));

        if (kv == null || kv.isExpired()) {
            return null;
        }

        return valueSerializer.deserialize(kv.getValue(), valueClass);
    }

    /**
     * 获取值，直到超时
     *
     * @param type             类型
     * @param key              键
     * @param retryDelayMillis 重试间隔（毫秒）
     * @param timeoutMillis    最大重试时间（毫秒）
     * @throws TimeoutException 获取超时时将抛出此异常
     */
    public <V> V get(
            String type,
            String key,
            Class<V> valueClass,
            int retryDelayMillis,
            int timeoutMillis
    ) throws TimeoutException {
        int retryCount = timeoutMillis / retryDelayMillis + 1;

        while (retryCount > 0) {
            V result = get(type, key, valueClass);
            if (result != null) {
                return result;
            }

            // 还未取得处理结果，循环等待
            retryCount--;
            ThreadUtil.sleep(retryDelayMillis);
        }

        throw new TimeoutException(String.format("type=%s|key=%s", type, key));
    }

    /**
     * 设置值
     */
    public <V> String put(String type, String key, V value) {
        return put(type, key, value, 0);
    }

    /**
     * 设置值
     *
     * @param ttlMillis 有效期（毫秒），0为永不过期
     */
    public <V> String put(String type, String key, V value, int ttlMillis) {
        return putWith(type, key, value, ttlMillis, it -> keyValueRepository().save(it));
    }

    /**
     * 仅在键不存在时能够成功设置值
     *
     * @return 若成功设置则返回key
     */
    public <V> String putIfAbsent(String type, String key, V value) {
        return putIfAbsent(type, key, value, 0);
    }

    /**
     * 仅在键不存在时能够成功设置值
     *
     * @param ttlMillis 有效期（毫秒），0为永不过期
     * @return 若成功设置则返回key
     */
    public <V> String putIfAbsent(String type, String key, V value, int ttlMillis) {
        return putWith(type, key, value, ttlMillis, it -> keyValueRepository().saveIfAbsent(it));
    }

    /**
     * 返回指定键值的过期时间戳（毫秒）
     *
     * @return -1为key不存在，0为key永不过期，其余为过期的标准时间戳
     */
    public long expirationTimestamp(String type, String key) {
        KeyValue kv = keyValueRepository.byId(KeyValueId.of(type, key));

        // 不存在
        if (kv == null || kv.isExpired()) {
            return -1;
        }

        // 永不过期
        if (kv.getExpirationTimestamp() == 0) {
            return 0;
        }

        // 过期时间戳
        return kv.getExpirationTimestamp();
    }

    private <V> String putWith(String type, String key, V value, int ttlMillis, Function<KeyValue, KeyValueId> func) {
        KeyValue kv = KeyValueFactory.create(
                type,
                key,
                valueSerializer.serialize(value),
                ttlMillis
        );

        // 过期键值不进行保存
        if (kv.isExpired()) {
            log.warn(LogMessage.create(this.getClass().getSimpleName(), "put")
                    .fail("KeyValue is expired")
                    .append("id", kv.id())
                    .toString());

            return null;
        }

        KeyValueId id = func.apply(kv);

        if (id == null) {
            return null;
        }

        return id.getName();
    }

    /**
     * 删除指定键值
     */
    public void remove(String type, String key) {
        keyValueRepository().remove(KeyValueId.of(type, key));
    }

    /**
     * 批量设置键值
     */
    public <V> void putAll(String type, Map<? extends String, ? extends V> m) {
        if (m == null) {
            return;
        }

        for (Map.Entry<? extends String, ? extends V> entry : m.entrySet()) {
            put(type, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 检查是否包含指定key
     */
    public boolean containsKey(String type, String key) {
        return keyValueRepository().containsKey(KeyValueId.of(type, key));
    }

    /**
     * 获取所有值集合
     */
    public <V> Collection<V> values(String type, Class<V> valueClass) {
        return keyValueRepository().byType(type)
                .stream()
                .filter(it -> !it.isExpired())
                .map(it -> valueSerializer.deserialize(it.getValue(), valueClass))
                .collect(Collectors.toSet());
    }

    /**
     * 获取所有键值集合
     */
    public <V> Set<KeyValueEntry<V>> entrySet(String type, Class<V> valueClass) {
        return keyValueRepository().byType(type)
                .stream()
                .map(it -> new KeyValueEntry<>(
                                type,
                                it.id().getName(),
                                valueSerializer.deserialize(it.getValue(), valueClass)
                        )
                )
                .collect(Collectors.toSet());
    }

    /**
     * 获取键值数量
     */
    public long size(String type) {
        return keyValueRepository().count(type);
    }

    /**
     * 清理键值
     */
    public void clear(String type) {
        keyValueRepository().clear(type);
    }

    /**
     * 获取键集合
     */
    public Set<String> keySet(String type) {
        return keyValueRepository().byType(type)
                .stream()
                .map(it -> it.id().getName())
                .collect(Collectors.toSet());
    }

    /**
     * 清理过期对象
     *
     * @param batchSize 一次最大删除数量
     */
    public void removeExpirationValues(int batchSize) {
        keyValueRepository().removeExpirationValues(batchSize);
    }

    /**
     * 当前键值是否为空
     */
    public boolean isEmpty(String type) {
        return size(type) == 0;
    }

    /**
     * 获取值转换器
     */
    public Serializer valueSerializer() {
        return valueSerializer;
    }

    public KeyValueRepository keyValueRepository() {
        return keyValueRepository;
    }

    public static class KeyValueEntry<V> {

        private final String type;
        private final String key;
        private final V value;

        public KeyValueEntry(String type, String key, V value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}