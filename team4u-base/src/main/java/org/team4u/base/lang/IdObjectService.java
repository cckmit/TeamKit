package org.team4u.base.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import org.team4u.base.bean.ServiceLoaderUtil;
import org.team4u.base.bean.event.AbstractBeanInitializedEventSubscriber;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.message.MessagePublisher;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 标识服务
 *
 * @param <K>
 * @param <V>
 * @author jay.wu
 * @see org.team4u.base.registrar.PolicyRegistrar
 * @deprecated 已废弃，使用PolicyRegistrar代替
 */
public abstract class IdObjectService<K, V extends IdObject<K>> {

    private final Log log = Log.get();

    private final Map<K, V> idObjectMap = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    private final Class<K> keyType = (Class<K>) ClassUtil.getTypeArgument(this.getClass());
    @SuppressWarnings("unchecked")
    private final Class<V> valueType = (Class<V>) ClassUtil.getTypeArgument(this.getClass(), 1);

    private final BeanInitializedEventSubscriber beanInitializedEventSubscriber = new BeanInitializedEventSubscriber();

    public IdObjectService() {
        this(Collections.emptyList());
    }

    public IdObjectService(Class<V> valueClass) {
        this(ServiceLoaderUtil.loadAvailableList(valueClass));
    }

    public IdObjectService(List<V> objects) {
        if (objects != null) {
            for (V obj : objects) {
                saveIdObject(obj);
            }
        }
    }

    /**
     * 根据通过BeanProviders和bean初始化事件保存value对象
     */
    public void saveObjectsByBeanProvidersAndEvent() {
        saveObjectsByBeanProviders(BeanProviders.getInstance());
        saveObjectsByBeanInitializedEvent();
    }

    /**
     * 通过BeanProviders保存value对象
     *
     * @param beanProviders bean提供者服务
     * @see BeanProviders
     */
    public void saveObjectsByBeanProviders(BeanProviders beanProviders) {
        saveIdObjects(beanProviders.getBeansOfType(valueType()).values());
    }

    /**
     * 通过监听BeanInitializedEvent，注册value对象
     * <p>
     * 注意，
     *
     * @see BeanInitializedEvent
     */
    public void saveObjectsByBeanInitializedEvent() {
        MessagePublisher.instance().subscribe(beanInitializedEventSubscriber);
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName 包路径
     * @param classFilter 类过滤器
     */
    public void saveObjectsByFilter(String packageName, Filter<Class<?>> classFilter) {
        saveObjectsByClasses(ClassUtil.scanPackage(packageName, classFilter));
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName 包路径
     * @param valueClass  类型
     */
    public void saveObjectsBySuper(String packageName, Class<?> valueClass) {
        saveObjectsByClasses(ClassUtil.scanPackageBySuper(packageName, valueClass));
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName     包路径
     * @param annotationClass 注解类型
     */
    public void saveObjectsByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        saveObjectsByClasses(ClassUtil.scanPackageByAnnotation(packageName, annotationClass));
    }

    @SuppressWarnings("unchecked")
    private void saveObjectsByClasses(Collection<Class<?>> classList) {
        classList.stream()
                .filter(ClassUtil::isNormalClass)
                .map(it -> (V) ReflectUtil.newInstanceIfPossible(it))
                .forEach(this::saveIdObject);
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
            throw new SystemDataNotExistException(valueType().getSimpleName() + "/" + id);
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

        if (log.isDebugEnabled()) {
            log.debug(LogMessage.create(this.getClass().getSimpleName(), "saveIdObject")
                    .append("id", v.id())
                    .append("value", v.toString())
                    .success()
                    .toString());
        }
    }

    /**
     * 保存对象集合
     */
    public void saveIdObjects(Collection<V> values) {
        Optional.ofNullable(values)
                .ifPresent(it -> it.forEach(this::saveIdObject));
    }

    /**
     * 获取所有注册对象集合
     */
    public List<V> idObjects() {
        return idObjectMap.values()
                .stream()
                .sorted(Comparator.comparingInt(IdObject::priority))
                .collect(Collectors.toList());
    }

    /**
     * 获取value类型
     *
     * @return value类型
     */
    @SuppressWarnings("unchecked")
    public Class<V> valueType() {
        return valueType;
    }

    /**
     * 获取key类型
     *
     * @return key类型
     */
    @SuppressWarnings("unchecked")
    public Class<K> keyType() {
        return keyType;
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

    private class BeanInitializedEventSubscriber extends AbstractBeanInitializedEventSubscriber {

        @Override
        protected void internalOnMessage(BeanInitializedEvent message) throws Exception {
            saveIdObject(message.getBean());
        }

        @Override
        public Class<?> getBeanType() {
            return valueType();
        }
    }
}