package org.team4u.base.filter;

import org.team4u.base.bean.event.ApplicationInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.AbstractMessageSubscriber;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于bean管理器的过滤器服务
 *
 * @author jay.wu
 * @see BeanProviders
 * @see ApplicationInitializedEvent
 */
public abstract class BeanFilterService<C> extends AbstractMessageSubscriber<ApplicationInitializedEvent>
        implements FilterService<C> {

    private SimpleFilterService<C> simpleFilterService;

    /**
     * 开始处理
     *
     * @param context 上下文
     * @return context 上下文
     */
    @Override
    public C doFilter(C context) {
        return simpleFilterService.doFilter(context);
    }

    @Override
    protected void internalOnMessage(ApplicationInitializedEvent message) throws Exception {
        simpleFilterService = new SimpleFilterService<>(filterClasses().stream()
                .map(it -> (Filter<C>) BeanProviders.getInstance().getBean(it))
                .collect(Collectors.toList()));
    }

    /**
     * 获取过滤器类型集合
     *
     * @return 返回过滤器类型集合
     */
    protected abstract List<Class<? extends Filter<C>>> filterClasses();

    @Override
    public Class<ApplicationInitializedEvent> messageType() {
        return ApplicationInitializedEvent.class;
    }
}