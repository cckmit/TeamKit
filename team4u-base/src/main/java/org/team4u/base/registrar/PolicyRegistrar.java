package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.Log;
import lombok.Getter;
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
 * 策略注册器
 *
 * @param <C>
 * @param <P>
 * @author jay.wu
 */
public abstract class PolicyRegistrar<C, P extends Policy<C>> {

    private final Log log = Log.get();

    private List<P> policies = new ArrayList<>();

    @Getter
    @SuppressWarnings("unchecked")
    private final Class<C> contextType = (Class<C>) ClassUtil.getTypeArgument(this.getClass());

    @Getter
    @SuppressWarnings("unchecked")
    private final Class<P> policyType = (Class<P>) ClassUtil.getTypeArgument(this.getClass(), 1);

    private final BeanInitializedEventSubscriber beanInitializedEventSubscriber = new BeanInitializedEventSubscriber();

    public PolicyRegistrar() {
        registerByServiceLoader();
    }

    public PolicyRegistrar(List<? extends P> policies) {
        register(policies);
    }

    /**
     * 通过ServiceLoader注册
     */
    public void registerByServiceLoader() {
        register(ServiceLoaderUtil.loadAvailableList(getPolicyType()));
    }


    /**
     * 根据通过BeanProviders和bean初始化事件注册
     */
    public void registerByBeanProvidersAndEvent() {
        registerByBeanProviders(BeanProviders.getInstance());
        registerByBeanInitializedEvent();
    }

    /**
     * 通过BeanProviders注册
     *
     * @param beanProviders bean提供者服务
     * @see BeanProviders
     */
    public void registerByBeanProviders(BeanProviders beanProviders) {
        register(beanProviders.getBeansOfType(getPolicyType()).values());
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
     * 通过扫描包注册
     *
     * @param packageName 包路径
     * @param classFilter 类过滤器
     */
    public void registerByFilter(String packageName, Filter<Class<?>> classFilter) {
        registerByClasses(ClassUtil.scanPackage(packageName, classFilter));
    }

    /**
     * 通过扫描包注册
     *
     * @param packageName 包路径
     * @param policyClass 类型
     */
    public void registerBySuper(String packageName, Class<?> policyClass) {
        registerByClasses(ClassUtil.scanPackageBySuper(packageName, policyClass));
    }

    /**
     * 通过扫描包注册
     *
     * @param packageName     包路径
     * @param annotationClass 注解类型
     */
    public void registerByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        registerByClasses(ClassUtil.scanPackageByAnnotation(packageName, annotationClass));
    }

    @SuppressWarnings("unchecked")
    private void registerByClasses(Collection<Class<?>> classList) {
        classList.stream()
                .filter(ClassUtil::isNormalClass)
                .map(it -> (P) ReflectUtil.newInstanceIfPossible(it))
                .forEach(this::register);
    }

    /**
     * 注册策略
     *
     * @param policy 策略
     */
    public void register(P policy) {
        policies.add(policy);

        // 每次注册后重新排序
        sortPolicies();

        if (log.isDebugEnabled()) {
            log.debug(LogMessage.create(this.getClass().getSimpleName(), "register")
                    .append("policy", policy.getClass().getName())
                    .success()
                    .toString());
        }
    }

    private void sortPolicies() {
        policies = policies.stream()
                .sorted(Comparator.comparingInt(Policy::priority))
                .collect(Collectors.toList());
    }

    /**
     * 注册策略结合
     *
     * @param policies 策略集合
     */
    public void register(Collection<? extends P> policies) {
        Optional.ofNullable(policies)
                .ifPresent(it -> it.forEach(this::register));
    }


    /**
     * 根据上下文获取策略
     */
    public P policyOf(C context) {
        return CollUtil.getFirst(policiesOf(context));
    }

    /**
     * 获取合适的策略
     *
     * @param context 上下文
     * @return 可用策略
     * @see SystemDataNotExistException 若策略不存在则抛出此异常
     */
    public P availablePolicyOf(C context) {
        P policy = policyOf(context);

        if (policy == null) {
            throw new SystemDataNotExistException("Unable to find a available policy|context=" + context);
        }

        return policy;
    }

    /**
     * 根据上下文获取策略集合
     */
    public List<P> policiesOf(C context) {
        return policies()
                .stream()
                .filter(it -> it.isSupport(context))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有注册策略集合
     */
    public List<P> policies() {
        return Collections.unmodifiableList(policies);
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

        protected BeanInitializedEventSubscriber() {
            super(policyType);
        }

        @Override
        protected void internalOnMessage(BeanInitializedEvent message) {
            //noinspection unchecked
            register((P) message.getBean());
        }
    }
}