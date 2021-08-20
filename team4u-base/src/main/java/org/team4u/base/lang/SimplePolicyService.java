package org.team4u.base.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import org.team4u.base.bean.ServiceLoaderUtil;
import org.team4u.base.bean.event.AbstractBeanInitializedEventSubscriber;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.message.MessagePublisher;
import org.team4u.base.registrar.PolicyRegistrar;

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
 * @see PolicyRegistrar
 * @deprecated 已废弃，使用PolicyRegistrar代替
 */
public abstract class SimplePolicyService<C, P extends SimplePolicy<C>> {

    private List<P> policies = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private final Class<P> policyType = (Class<P>) ClassUtil.getTypeArgument(this.getClass(), 1);

    private final BeanInitializedEventSubscriber beanInitializedEventSubscriber = new BeanInitializedEventSubscriber();

    public SimplePolicyService() {
        this(Collections.emptyList());
    }

    public SimplePolicyService(Class<P> policyClass) {
        this(ServiceLoaderUtil.loadAvailableList(policyClass));
    }

    public SimplePolicyService(List<P> policies) {
        if (policies != null) {
            for (P obj : policies) {
                register(obj);
            }
        }
    }

    /**
     * 通过监听BeanInitializedEvent，注册策略
     *
     * @see BeanInitializedEvent
     */
    public void registerByBeanInitializedEvent() {
        MessagePublisher.instance().subscribe(beanInitializedEventSubscriber);
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
     * @param policyClass 类型
     */
    public void registerBySuper(String packageName, Class<?> policyClass) {
        register(ClassUtil.scanPackageBySuper(packageName, policyClass));
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
                .map(it -> (P) ReflectUtil.newInstanceIfPossible(it))
                .forEach(this::register);
    }

    /**
     * 根据上下文获取策略
     */
    public SimplePolicy<C> policyOf(C context) {
        return CollUtil.getFirst(policiesOf(context));
    }

    /**
     * 根据上下文获取策略集合
     */
    public List<SimplePolicy<C>> policiesOf(C context) {
        return policies.stream()
                .filter(it -> it.isSupport(context))
                .collect(Collectors.toList());
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
     * @param policy 策略
     */
    public void register(P policy) {
        policies.add(policy);
    }

    /**
     * 获取所有注册策略集合
     */
    public Collection<P> policies() {
        return Collections.unmodifiableCollection(policies);
    }

    /**
     * 删除策略
     */
    public void unregister(P policy) {
        policies = policies.stream()
                .filter(it -> !policy.getClass().equals(it.getClass()))
                .collect(Collectors.toList());
    }

    private class BeanInitializedEventSubscriber extends AbstractBeanInitializedEventSubscriber {

        @Override
        protected void internalOnMessage(BeanInitializedEvent message) {
            //noinspection unchecked
            register((P) message.getBean());
        }

        @Override
        public Class<?> getBeanType() {
            return policyType;
        }
    }
}