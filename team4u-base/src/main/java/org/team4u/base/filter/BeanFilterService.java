package org.team4u.base.filter;

import org.team4u.base.bean.provider.BeanProviders;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于bean管理器的过滤器服务
 *
 * @author jay.wu
 * @see BeanProviders
 */
public abstract class BeanFilterService<C> extends FilterService<C> {

    /**
     * 根据过滤器类型集合构建责任链
     */
    @PostConstruct
    public void buildFilterChainByFilterTypes() {
        buildFilterChainByFilters(filterClasses().stream()
                .map(it -> (Filter<C>) BeanProviders.getInstance().getBean(it))
                .collect(Collectors.toList()));
    }

    /**
     * 获取过滤器类型集合
     *
     * @return 返回过滤器类型集合
     */
    protected abstract List<Class<? extends Filter<C>>> filterClasses();
}