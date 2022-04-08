package org.team4u.base.bean.provider;


import cn.hutool.core.lang.Dict;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.message.jvm.MessagePublisher;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于本地缓存的bean提供者
 *
 * @author jay.wu
 */
public class LocalBeanProvider implements BeanProvider {

    public static final String ID = "LOCAL";

    private final Dict beans = Dict.create();

    @Override
    public <T> T getBean(String name) {
        return beans.getBean(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> type) {
        return (T) beans.values()
                .stream()
                .filter(it -> type.isAssignableFrom(it.getClass()))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return beans.entrySet()
                .stream()
                .filter(it -> type.isAssignableFrom(it.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, it -> (T) it.getValue()));
    }

    @Override
    public <T> boolean registerBean(String beanName, T bean) {
        // 重复注册，直接返回true
        if (beans.containsKey(beanName)) {
            return true;
        }

        beans.set(beanName, bean);
        MessagePublisher.instance().publish(new BeanInitializedEvent(beanName, bean));
        return true;
    }

    public void unregisterAllBeans() {
        beans.clear();
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public String id() {
        return ID;
    }
}