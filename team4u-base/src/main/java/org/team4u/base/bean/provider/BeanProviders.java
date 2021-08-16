package org.team4u.base.bean.provider;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.lang.IdObjectService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * bean提供者服务
 *
 * @author jay.wu
 */
public class BeanProviders extends IdObjectService<String, BeanProvider> {

    private static final BeanProviders instance = new BeanProviders();

    public static BeanProviders getInstance() {
        return instance;
    }

    public BeanProviders() {
        super(BeanProvider.class);

        sortByPriority();
    }

    public BeanProviders(List<BeanProvider> objects) {
        super(objects);

        sortByPriority();
    }

    private void sortByPriority() {
        saveIdObjects(CollUtil.sortByProperty(idObjects(), "priority"));
    }

    /**
     * 通过name获取 Bean
     *
     * @param <T>  Bean类型
     * @param name Bean名称
     * @return Bean
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return idObjects()
                .stream()
                .map(it -> (T) it.getBean(name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 通过class获取Bean
     *
     * @param <T>  Bean类型
     * @param type Bean类
     * @return Bean对象
     */
    public <T> T getBean(Class<T> type) {
        return idObjects()
                .stream()
                .map(it -> it.getBean(type))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取指定类型对应的所有Bean，包括子类
     *
     * @param <T>  Bean类型
     * @param type 类、接口，null表示获取所有bean
     * @return 类型对应的bean，key是bean注册的name，value是Bean
     */
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return idObjects()
                .stream()
                .map(it -> it.getBeansOfType(type))
                .filter(Objects::nonNull)
                .flatMap(it -> it.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 注册Bean
     *
     * @param <T>  Bean类型
     * @param bean bean
     */
    public <T> boolean registerBean(String beanName, T bean) {
        return idObjects()
                .stream()
                .map(it -> it.registerBean(beanName, bean))
                .filter(it -> it)
                .findFirst()
                .orElse(false);
    }

    /**
     * 注册Bean
     *
     * @param <T>  Bean类型
     * @param bean bean
     */
    public <T> boolean registerBean(T bean) {
        return idObjects()
                .stream()
                .map(it -> it.registerBean(bean))
                .filter(it -> it)
                .findFirst()
                .orElse(false);
    }
}