package org.team4u.base.bean.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.log.LogMessage;
import org.team4u.base.registrar.PolicyRegistrar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * bean提供者服务
 * <p>
 * 设计目的：
 * - 很多通用服务需要一套完整的扩展服务，配合PolicyRegistrar可以实现一部分需求，但无法方便地与spring联动
 * - 该类提供一套简单通用的bean管理功能供通用服务扩展，在与特定框架解耦（如Spring）的同时，通过监听BeanInitializedEvent实现特定框架的回调注册
 *
 * @author jay.wu
 * @see PolicyRegistrar
 * @see BeanInitializedEvent
 */
@Slf4j
public class BeanProviders extends PolicyRegistrar<String, BeanProvider> {

    public static BeanProviders getInstance() {
        return Singleton.get(BeanProviders.class);
    }

    public BeanProviders() {
    }

    public BeanProviders(List<BeanProvider> objects) {
        super(objects);
    }

    /**
     * 获取bean对象
     * <p>
     * 先尝试按指定bean标识获取，若不指定，则尝试按照类型获取
     *
     * @param beanType    bean类型
     * @param beanIdRegex bean标识正则表达式
     * @param <T>         bean类型
     * @return bean对象
     * @throws NoSuchBeanDefinitionException 无法找到bean时抛出此异常
     */
    public <T> T getBean(Class<T> beanType, String beanIdRegex) throws NoSuchBeanDefinitionException {
        // 按照指定bean标识表达式查找
        if (StrUtil.isNotBlank(beanIdRegex)) {
            return getBean(beanType, name -> ReUtil.isMatch(beanIdRegex, name));
        }

        // 未指定bean标识，尝试按类型查找
        return getBean(beanType);
    }

    /**
     * 获取bean对象
     *
     * @param beanType       bean类型
     * @param beanNameFilter bean名称过滤器
     * @param <T>            bean类型
     * @return bean对象
     * @throws NoSuchBeanDefinitionException 无法找到bean时抛出此异常
     */
    public <T> T getBean(Class<T> beanType,
                         Function<String, Boolean> beanNameFilter) throws NoSuchBeanDefinitionException {
        return getBeansOfType(beanType).entrySet().stream()
                .filter(it -> beanNameFilter.apply(it.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException("beanType=" + beanType.getName()));
    }

    /**
     * 通过name获取 Bean
     *
     * @param <T>  Bean类型
     * @param name Bean名称
     * @return Bean
     * @throws NoSuchBeanDefinitionException 无法找到bean时抛出此异常
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) throws NoSuchBeanDefinitionException {
        return policies()
                .stream()
                .map(it -> (T) it.getBean(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException("name=" + name));
    }

    /**
     * 通过class获取Bean
     *
     * @param <T>  Bean类型
     * @param type Bean类
     * @return Bean对象
     * @throws NoSuchBeanDefinitionException 无法找到bean时抛出此异常
     */
    public <T> T getBean(Class<T> type) throws NoSuchBeanDefinitionException {
        return policies()
                .stream()
                .map(it -> it.getBean(type))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException("beanType=" + type.getName()));
    }

    /**
     * 获取指定类型对应的所有Bean，包括子类
     * <p>
     * 如果存在同名n，将返回优先级高的bean
     *
     * @param <T>  Bean类型
     * @param type 类、接口，null表示获取所有bean
     * @return 类型对应的bean，key是bean注册的name，value是Bean
     */
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        // 反转以确保高优先级的key不会被低优先级的覆盖
        return CollUtil.reverse(policies())
                .stream()
                .map(it -> it.getBeansOfType(type))
                .reduce(new HashMap<>(), (a, b) -> {
                    a.putAll(b);
                    return a;
                });
    }

    /**
     * 注册Bean
     * <p>
     * 注册成功后将发布BeanInitializedEvent事件
     *
     * @param <T>  Bean类型
     * @param bean bean
     * @see BeanInitializedEvent
     */
    public <T> boolean registerBean(String beanName, T bean) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "registerBean")
                .append("beanName", beanName)
                .append("bean", bean);

        boolean result = policies()
                .stream()
                .map(it -> it.registerBean(beanName, bean))
                .filter(it -> it)
                .findFirst()
                .orElse(false);

        if (result) {
            lm.success();
        } else {
            lm.fail();
        }

        log.info(lm.toString());
        return result;
    }

    /**
     * 注册Bean
     *
     * @param <T>  Bean类型
     * @param bean bean
     */
    public <T> boolean registerBean(T bean) {
        return registerBean(bean.getClass().getSimpleName(), bean);
    }

    public LocalBeanProvider local() {
        return (LocalBeanProvider) BeanProviders.getInstance().policyOf(LocalBeanProvider.ID);
    }
}