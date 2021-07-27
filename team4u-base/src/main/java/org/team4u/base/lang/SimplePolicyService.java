package org.team4u.base.lang;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.base.error.SystemDataNotExistException;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 简单策略服务
 *
 * @param <C> 策略上下文
 * @param <P> 策略类型
 * @author jay.wu
 */
public abstract class SimplePolicyService<C, P extends SimplePolicy<C>> {

    private List<P> policies = new ArrayList<>();

    public SimplePolicyService() {
        this(Collections.emptyList());
    }

    public SimplePolicyService(Class<P> valueClass) {
        this(ServiceLoaderUtil.loadAvailableList(valueClass));
    }

    public SimplePolicyService(List<P> objects) {
        if (objects != null) {
            for (P obj : objects) {
                register(obj);
            }
        }
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName 包路径
     * @param classFilter 类过滤器
     */
    public void registerByFilter(String packageName, Filter<Class<?>> classFilter) {
        register(ClassUtil.scanPackage(packageName, classFilter));
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName 包路径
     * @param valueClass  类型
     */
    public void registerBySuper(String packageName, Class<?> valueClass) {
        register(ClassUtil.scanPackageBySuper(packageName, valueClass));
    }

    /**
     * 通过扫描包创建服务
     *
     * @param packageName     包路径
     * @param annotationClass 注解类型
     */
    public void registerByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        register(ClassUtil.scanPackageByAnnotation(packageName, annotationClass));
    }

    @SuppressWarnings("unchecked")
    private void register(Collection<Class<?>> classList) {
        classList.stream()
                .filter(ClassUtil::isNormalClass)
                .map(it -> (P) ReflectUtil.newInstance(it))
                .forEach(this::register);
    }

    /**
     * 根据标识获取策略
     */
    public SimplePolicy<C> policyOf(C context) {
        return policies.stream()
                .filter(it -> it.isSupport(context))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取合适的策略
     *
     * @param context 上下文
     * @return 可用策略
     * @see SystemDataNotExistException 若策略不存在则抛出此异常
     */
    public SimplePolicy<C> availablePolicyOf(C context) {
        SimplePolicy<C> policy = policyOf(context);

        if (policy == null) {
            throw new SystemDataNotExistException("Unable to find a available policy|context=" + context);
        }

        return policy;
    }

    /**
     * 注册策略
     *
     * @param p 策略
     */
    public void register(P p) {
        policies.add(p);
    }

    /**
     * 获取所有注册策略集合
     */
    public List<P> policies() {
        return policies;
    }

    /**
     * 删除策略
     */
    public void unregister(P policy) {
        policies = policies.stream()
                .filter(it -> !policy.getClass().equals(it.getClass()))
                .collect(Collectors.toList());
    }
}