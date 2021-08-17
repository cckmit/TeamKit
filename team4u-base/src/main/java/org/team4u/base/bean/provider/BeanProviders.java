package org.team4u.base.bean.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * bean提供者服务
 *
 * @author jay.wu
 */
@Slf4j
public class BeanProviders extends IdObjectService<String, BeanProvider> {

    public static BeanProviders getInstance() {
        return Singleton.get(BeanProviders.class);
    }

    public BeanProviders() {
        super(BeanProvider.class);
    }

    public BeanProviders(List<BeanProvider> objects) {
        super(objects);
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
        return idObjects()
                .stream()
                .map(it -> (T) it.getBean(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException(name));
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
        return idObjects()
                .stream()
                .map(it -> it.getBean(type))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanDefinitionException(type.getName()));
    }

    /**
     * 获取指定类型对应的所有Bean，包括子类
     *
     * @param <T>  Bean类型
     * @param type 类、接口，null表示获取所有bean
     * @return 类型对应的bean，key是bean注册的name，value是Bean
     */
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        // 反转以确保高优先级的key不会被低优先级的覆盖
        return CollUtil.reverse(idObjects())
                .stream()
                .map(it -> it.getBeansOfType(type))
                .reduce(new HashMap<>(), (a, b) -> {
                    a.putAll(b);
                    return a;
                });
    }

    /**
     * 注册Bean
     *
     * @param <T>  Bean类型
     * @param bean bean
     */
    public <T> boolean registerBean(String beanName, T bean) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "registerBean")
                .append("beanName", beanName)
                .append("bean", bean);

        boolean result = idObjects()
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
}