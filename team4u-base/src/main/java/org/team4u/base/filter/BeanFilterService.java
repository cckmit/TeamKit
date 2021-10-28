package org.team4u.base.filter;

import org.team4u.base.bean.event.ApplicationInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.MessageSubscriber;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于bean管理器的过滤器服务
 *
 * @author jay.wu
 * @see BeanProviders
 * @see ApplicationInitializedEvent
 */
public abstract class BeanFilterService<C> extends FilterService<C> implements MessageSubscriber<ApplicationInitializedEvent> {

    /**
     * 根据过滤器类型集合构建责任链
     */
    @Override
    public void onMessage(ApplicationInitializedEvent event) {
        setFilterChain(
                FilterChain.create(filterClasses().stream()
                        .map(it -> (Filter<C>) BeanProviders.getInstance().getBean(it))
                        .collect(Collectors.toList()))
        );
    }

    /**
     * 获取过滤器类型集合
     *
     * @return 返回过滤器类型集合
     */
    protected abstract List<Class<? extends Filter<C>>> filterClasses();
}