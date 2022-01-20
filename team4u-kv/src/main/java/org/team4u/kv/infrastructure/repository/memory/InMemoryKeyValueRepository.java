package org.team4u.kv.infrastructure.repository.memory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.kv.KeyValueRepository;
import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于内存的kv存储
 * <p>
 * 底层实现为map，仅用于测试
 */
public class InMemoryKeyValueRepository implements KeyValueRepository {

    private final Log log = LogFactory.get();

    private final Map<String, KeyValue> cache;

    public InMemoryKeyValueRepository() {
        this(new ConcurrentHashMap<>());
    }

    public InMemoryKeyValueRepository(Map<String, KeyValue> cache) {
        this.cache = cache;
    }

    @Override
    public KeyValue byId(KeyValueId id) {
        return cache.get(id.toString());
    }

    @Override
    public List<KeyValue> byType(String type) {
        return cache.values()
                .stream()
                .filter(it -> StrUtil.equals(it.id().getType(), type))
                .collect(Collectors.toList());
    }

    @Override
    public int removeExpirationValues(int maxBatchSize) {
        int count = 0;

        for (KeyValue keyValue : cache.values()) {
            if (count == maxBatchSize) {
                break;
            }

            if (keyValue.isExpired()) {
                remove(keyValue.id());

                count++;

                log.debug(LogMessage.create(this.getClass().getSimpleName(), "removeExpirationValues")
                        .success()
                        .append("id", keyValue.id())
                        .append("isExpired", true)
                        .toString());
            }
        }

        return count;
    }

    @Override
    public KeyValueId save(KeyValue keyValue) {
        cache.put(keyValue.id().toString(), keyValue);
        return keyValue.id();
    }

    @Override
    public KeyValueId saveIfAbsent(KeyValue keyValue) {
        // 先删除过期kv
        KeyValue kv = byId(keyValue.id());
        if (kv != null && kv.isExpired()) {
            remove(kv.id());
        }

        // 再尝试插入
        KeyValue oldKv = cache.putIfAbsent(keyValue.id().toString(), keyValue);
        if (oldKv == null) {
            return keyValue.id();
        } else {
            return null;
        }
    }

    @Override
    public void clear(String type) {
        for (KeyValue keyValue : byType(type)) {
            remove(keyValue.id());
        }
    }

    @Override
    public void remove(KeyValueId id) {
        cache.remove(id.toString());
    }

    @Override
    public long count(String type) {
        return byType(type).size();
    }

    @Override
    public boolean containsKey(KeyValueId id) {
        return byId(id) != null;
    }

    public Map<String, KeyValue> cache() {
        return cache;
    }
}