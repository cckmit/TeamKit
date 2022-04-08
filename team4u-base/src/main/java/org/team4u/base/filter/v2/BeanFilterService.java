package org.team4u.base.filter.v2;

import lombok.Getter;
import org.team4u.base.bean.event.ApplicationInitializedEvent;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;

/**
 * 基于bean管理器的过滤器服务
 *
 * @author jay.wu
 * @see BeanProviders
 * @see ApplicationInitializedEvent
 */
public abstract class BeanFilterService<C, F extends Filter<C>> extends AbstractMessageSubscriber<ApplicationInitializedEvent> {

    @Getter
    private FilterChain<C, F> filterChain;

    @Override
    protected void internalOnMessage(ApplicationInitializedEvent message) throws Exception {
        filterChain = FilterChain.create(config());
    }

    /**
     * 执行过滤
     *
     * @param context 过滤上下文
     */
    public C doFilter(C context) {
        return filterChain.doFilter(context);
    }

    /**
     * 获取过滤器责任链配置
     */
    protected abstract FilterChain.Config config();

    @Override
    public Class<ApplicationInitializedEvent> messageType() {
        return ApplicationInitializedEvent.class;
    }
}