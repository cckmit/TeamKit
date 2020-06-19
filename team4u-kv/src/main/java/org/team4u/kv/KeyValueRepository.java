package org.team4u.kv;

import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueId;

import java.util.List;

/**
 * 键值资源库
 * <p>
 * 用于持久化键值对象
 *
 * @author jay.wu
 */
public interface KeyValueRepository {

    /**
     * 根据标识获取kv对象
     */
    KeyValue byId(KeyValueId id);

    /**
     * 根据类型获取kv对象集合
     */
    List<KeyValue> byType(String type);

    /**
     * 清理过期对象
     *
     * @param maxBatchSize 一次最大删除数量
     * @return 成功删除数量
     */
    int removeExpirationValues(int maxBatchSize);

    /**
     * 保存kv对象
     */
    KeyValueId save(KeyValue keyValue);

    /**
     * 仅在键不存在时能够成功保存值
     *
     * @return 若成功保存则返回标识
     */
    KeyValueId saveIfAbsent(KeyValue keyValue);

    /**
     * 清理kv
     */
    void clear(String type);

    /**
     * 删除指定标识kv
     */
    void remove(KeyValueId id);

    /**
     * 统计kv数量
     */
    int count(String type);

    /**
     * 检查是否包含指定标识数据
     */
    boolean containsKey(KeyValueId id);
}