package org.team4u.base.bean.provider;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.team4u.base.bean.event.BeanInitializedEvent;
import org.team4u.base.message.MessagePublisher;

import java.util.Collections;
import java.util.Map;

/**
 * 基于spring的bean提供者
 *
 * @author jay.wu
 * @see SpringUtil
 */
public class SpringBeanProvider implements BeanProvider {

    public static final String ID = "SPRING";

    @Override
    public <T> T getBean(String name) {
        if (!isSpringContext()) {
            return null;
        }

        try {
            return SpringUtil.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public <T> T getBean(Class<T> type) {
        if (!isSpringContext()) {
            return null;
        }

        try {
            return SpringUtil.getBean(type);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        if (!isSpringContext()) {
            return Collections.emptyMap();
        }

        try {
            return SpringUtil.getBeansOfType(type);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    @Override
    public <T> boolean registerBean(String beanName, T bean) {
        if (!isSpringContext()) {
            return false;
        }

        SpringUtil.registerBean(beanName, bean);
        MessagePublisher.instance().publish(new BeanInitializedEvent(beanName, bean));
        return true;
    }

    @Override
    public int priority() {
        return 50;
    }

    @Override
    public String id() {
        return ID;
    }

    private boolean isSpringContext() {
        try {
            return SpringUtil.getBeanFactory() != null;
        } catch (Exception e) {
            return false;
        }
    }
}