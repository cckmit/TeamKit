package org.team4u.base.registrar;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import lombok.Getter;
import org.team4u.base.bean.ServiceLoaderUtil;
import org.team4u.base.bean.event.AbstractBeanInitializedEventSubscriber;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.log.LogMessage;
import org.team4u.base.message.MessagePublisher;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 策略注册器
 *
 * @param <C> 策略上下文类型
 * @param <P> 策略类型
 * @author jay.wu
 */
public abstract class PolicyRegistrar<C, P extends Policy<C>> {

    private final Log log = Log.get();
    @Getter
    @SuppressWarnings("unchecked")
    private final Class<C> contextType = (Class<C>) ClassUtil.getTypeArgument(this.getClass());
    @Getter
    @SuppressWarnings("unchecked")
    private final Class<P> policyType = (Class<P>) ClassUtil.getTypeArgument(this.getClass(), 1);
    private final BeanInitializedEventSubscriber beanInitializedEventSubscriber = new BeanInitializedEventSubscriber();
    private List<P> policies = new ArrayList<>();

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
        if (policy == null) {
            return;
        }

        policies.add(policy);

        // 每次注册后重新排序
        sortPolicies();

        log.info(LogMessage.create(this.getClass().getSimpleName(), "register")
                .append("policy", policy.policyName())
                .append("priority", policy.priority())
                .append("policies", policyNames())
                .success()
                .toString());
    }

    /**
     * 对策略集合排序
     * <p>
     * 确保策略集合按优先级从高到低保存
     */
    private void sortPolicies() {
        policies = policies.stream()
                .sorted(Comparator.comparingInt(Policy::priority))
                .collect(Collectors.toList());
    }

    /**
     * 注册策略集合
     *
     * @param policies 策略集合
     */
    public void register(Collection<? extends P> policies) {
        Optional.ofNullable(policies)
                .ifPresent(it -> it.forEach(this::register));
    }


    /**
     * 根据上下文获取策略
     *
     * @return 返回匹配上下文且优先级最高的策略
     */
    public P policyOf(C context) {
        return CollUtil.getFirst(policiesOf(context));
    }

    /**
     * 获取合适的策略
     *
     * @param context 上下文
     * @return 返回匹配上下文且优先级最高的策略
     * @see NoSuchPolicyException 若无匹配的策略则抛出此异常
     */
    public P availablePolicyOf(C context) throws NoSuchPolicyException {
        return policiesStreamOf(context)
                .findFirst()
                .orElseThrow(() -> new NoSuchPolicyException("Unable to find policy|context=" + context));
    }

    /**
     * 根据上下文获取策略集合
     *
     * @return 返回所有匹配上下文的策略集合
     */
    public List<P> policiesOf(C context) {
        return policiesStreamOf(context).collect(Collectors.toList());
    }

    private Stream<P> policiesStreamOf(C context) {
        return policies.stream().filter(it -> it.supports(context));
    }

    /**
     * 获取所有注册策略集合
     */
    public List<P> policies() {
        return new ArrayList<>(policies);
    }

    /**
     * 删除策略
     */
    public void unregister(P policy) {
        policies = policies.stream()
                .filter(it -> !ObjectUtil.equals(it, policy))
                .collect(Collectors.toList());

        log.info(LogMessage.create(this.getClass().getSimpleName(), "unregister")
                .append("policy", policy.policyName())
                .append("policies", policyNames())
                .success()
                .toString());
    }

    /**
     * 删除策略
     */
    public void unregister(String policyName) {
        policies = policies.stream()
                .filter(it -> !StrUtil.equals(it.policyName(), policyName))
                .collect(Collectors.toList());

        log.info(LogMessage.create(this.getClass().getSimpleName(), "unregister")
                .append("policy", policyName)
                .append("policies", policyNames())
                .success()
                .toString());
    }

    private List<String> policyNames(){
       return policies.stream().map(Policy::policyName).collect(Collectors.toList());
    }

    private class BeanInitializedEventSubscriber extends AbstractBeanInitializedEventSubscriber {

        @Override
        protected void internalOnMessage(BeanInitializedEvent message) {
            //noinspection unchecked
            register((P) message.getBean());
        }

        @Override
        public Class<?> getBeanType() {
            return getPolicyType();
        }
    }
}