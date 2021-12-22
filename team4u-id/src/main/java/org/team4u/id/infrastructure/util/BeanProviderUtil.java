package org.team4u.id.infrastructure.util;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.bean.provider.NoSuchBeanDefinitionException;

/**
 * BeanProvider工具类
 *
 * @author jay.wu
 */
public class BeanProviderUtil {

    /**
     * 获取bean对象
     * <p>
     * 先尝试按指定bean标识获取，若不指定，则尝试按照类型获取
     *
     * @param beanType bean类型
     * @param beanId   bean标识
     * @param <T>      bean类型
     * @return bean对象
     * @throws NoSuchBeanDefinitionException 无法找到bean时抛出此异常
     */
    public static <T> T getBean(Class<T> beanType, String beanId) throws NoSuchBeanDefinitionException {
        // 按照指定bean标识查找
        if (StrUtil.isNotBlank(beanId)) {
            return BeanProviders.getInstance().getBean(beanId);
        }

        // 未指定bean标识，尝试按类型查找
        return BeanProviders.getInstance().getBean(beanType);
    }
}