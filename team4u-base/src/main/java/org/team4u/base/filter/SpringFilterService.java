package org.team4u.base.filter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于Spring的过滤器服务
 *
 * @author jay.wu
 */
public abstract class SpringFilterService<C> extends SimpleFilterService<C>
        implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    /**
     * 根据过滤器类型集合构建责任链
     */
    public void initFilterChain() {
        setFilterChain(
                FilterChain.create(filterClasses().stream()
                        .map(it -> (Filter<C>) applicationContext.getBean(it))
                        .collect(Collectors.toList()))
        );
    }

    @Override
    public void afterPropertiesSet() {
        initFilterChain();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取过滤器类型集合
     *
     * @return 返回过滤器类型集合
     */
    protected abstract List<Class<? extends Filter<C>>> filterClasses();
}