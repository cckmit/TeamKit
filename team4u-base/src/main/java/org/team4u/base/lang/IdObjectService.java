package org.team4u.base.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import org.team4u.base.error.SystemDataNotExistException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 标识服务
 *
 * @param <K>
 * @param <V>
 * @author jay.wu
 */
public abstract class IdObjectService<K, V extends IdObject<K>> {

    private final Map<K, V> idObjectMap = new ConcurrentHashMap<>();

    public IdObjectService() {
        this(null);
    }

    public IdObjectService(List<V> objects) {
        if (objects != null) {
            for (V obj : objects) {
                saveIdObject(obj);
            }
        }
    }

    /**
     * 根据标识获取对象
     */
    public V objectOfId(K id) {
        return idObjectMap.get(id);
    }

    /**
     * 获取对象集合
     *
     * @param idList 对象标识集合
     * @return 对象集合
     */
    public List<V> objectsOfId(List<K> idList) {
        if (CollUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }

        return idList.stream()
                .map(this::objectOfId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取可用的对象
     *
     * @param id 对象标识
     * @return 可用对象
     * @see SystemDataNotExistException 若对象不存在则抛出此异常
     */
    public V availableObjectOfId(K id) {
        V object = objectOfId(id);

        if (object == null) {
            throw new SystemDataNotExistException(valueType().getSimpleName() + "=" + id);
        }

        return object;
    }

    /**
     * 获取可用的对象集合
     *
     * @param idList 对象标识集合
     * @return 对象集合
     * @see SystemDataNotExistException 若对象不存在则抛出此异常
     */
    public List<V> availableObjectsOfId(List<K> idList) {
        if (CollUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }

        return idList.stream()
                .map(this::availableObjectOfId)
                .collect(Collectors.toList());
    }

    /**
     * 保存对象
     *
     * @param v 对象
     */
    public void saveIdObject(V v) {
        idObjectMap.put(v.id(), v);
    }

    /**
     * 获取所有注册对象集合
     */
    public Collection<V> idObjects() {
        return idObjectMap.values();
    }

    /**
     * 获取value类型
     *
     * @return value类型
     */
    @SuppressWarnings("unchecked")
    public Class<V> valueType() {
        return (Class<V>) ClassUtil.getTypeArgument(this.getClass());
    }

    /**
     * 获取key类型
     *
     * @return key类型
     */
    @SuppressWarnings("unchecked")
    public Class<K> keyType() {
        return (Class<K>) ClassUtil.getTypeArgument(this.getClass(), 1);
    }

    /**
     * 添加新对象
     *
     * @param key   键
     * @param value 值
     */
    public void addObject(K key, V value) {
        idObjectMap.put(key, value);
    }

    /**
     * 批量添加对象
     *
     * @param objects 对象集合
     */
    public void addObjects(List<V> objects) {
        for (V object : objects) {
            addObject(object.id(), object);
        }
    }

    /**
     * 删除对象
     *
     * @param key 键
     */
    public void removeObject(K key) {
        idObjectMap.remove(key);
    }

    /**
     * 批量删除对象
     *
     * @param keyList 键集合
     */
    public void removeObjects(List<K> keyList) {
        for (K key : keyList) {
            removeObject(key);
        }
    }
}