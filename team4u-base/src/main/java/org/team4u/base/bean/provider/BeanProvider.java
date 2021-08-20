package org.team4u.base.bean.provider;

import org.team4u.base.registrar.StringIdPolicy;

import java.util.Map;

/**
 * bean提供者
 *
 * @author jay.wu
 */
public interface BeanProvider extends StringIdPolicy {

    /**
     * 通过name获取 Bean
     *
     * @param <T>  Bean类型
     * @param name Bean名称
     * @return Bean
     */
    <T> T getBean(String name);


    /**
     * 通过class获取Bean
     *
     * @param <T>  Bean类型
     * @param type Bean类
     * @return Bean对象
     */
    <T> T getBean(Class<T> type);


    /**
     * 获取指定类型对应的所有Bean，包括子类
     *
     * @param <T>  Bean类型
     * @param type 类、接口，null表示获取所有bean
     * @return 类型对应的bean，key是bean注册的name，value是Bean
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);


    /**
     * 注册Bean
     *
     * @param <T>      Bean类型
     * @param beanName 名称
     * @param bean     bean
     */
    <T> boolean registerBean(String beanName, T bean);
}