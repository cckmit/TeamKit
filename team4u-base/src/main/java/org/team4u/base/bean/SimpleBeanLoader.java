package org.team4u.base.bean;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.extra.spring.SpringUtil;
import org.team4u.base.error.BeanNotExistException;
import org.team4u.base.message.MessagePublisher;

import java.util.function.Supplier;

/**
 * 基于Spring或者本地缓存的bean加载器
 *
 * @author jay.wu
 */
public class SimpleBeanLoader {

    /**
     * 获取非空的bean
     *
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean
     */
    public static <T> T getAvailableBean(Class<T> clazz) {
        T bean = getBean(clazz);
        Assert.notNull(bean, (Supplier<RuntimeException>) () -> new BeanNotExistException(clazz.getName()));
        return bean;
    }

    /**
     * 获取非空的bean
     *
     * @param name bean的名称
     * @param <T>  bean类型
     * @return bean
     */
    public static <T> T getAvailableBean(String name) {
        T bean = getBean(name);
        Assert.notNull(bean, (Supplier<RuntimeException>) () -> new BeanNotExistException(name));
        return bean;
    }

    /**
     * 获取bean
     *
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        if (isSpringContext()) {
            T bean = SpringUtil.getBean(clazz);
            if (bean != null) {
                return bean;
            }
        }

        return Singleton.get(clazz.getName(), (Func0<T>) () -> null);
    }

    /**
     * 获取bean
     *
     * @param name bean的名称
     * @param <T>  bean类型
     * @return bean
     */
    public static <T> T getBean(String name) {
        if (isSpringContext()) {
            T bean = SpringUtil.getBean(name);
            if (bean != null) {
                return bean;
            }
        }

        return Singleton.get(name, (Func0<T>) () -> null);
    }

    /**
     * 注册bean
     */
    public static <T> void registerBean(T bean) {
        registerBean(bean.getClass().getSimpleName(), bean);
    }

    /**
     * 注册bean
     */
    public static <T> void registerBean(String beanName, T bean) {
        if (isSpringContext()) {
            SpringUtil.registerBean(beanName, bean);
        } else {
            Singleton.put(beanName, bean);
        }

        MessagePublisher.instance().publish(new BeanInitializedEvent(bean));
    }

    private static boolean isSpringContext() {
        return SpringUtil.getBeanFactory() != null;
    }
}