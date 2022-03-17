package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤器责任链
 *
 * @param <C> 上下文类型
 * @author jay.wu
 */
public class FilterChain<C, F extends Filter<C>> {

    private final Log log = Log.get();

    @Getter
    private final Config config;

    /**
     * 第一个过滤器执行者
     */
    private FilterInvoker<C> header;

    private final FilterInterceptorService<C, F> interceptorService;

    public FilterChain(Config config) {
        this.config = config;
        this.interceptorService = new FilterInterceptorService<>(config.getInterceptors());

        initFilterChain();
    }

    @SuppressWarnings("unchecked")
    private void initFilterChain() {
        FilterInvoker<C> last = FilterInvoker.EMPTY_INVOKER;

        for (F filter : CollUtil.reverse((List<F>) config.filtersWithClasses())) {
            FilterInvoker<C> next = last;
            last = context -> interceptorService.apply(context, next, filter);
        }

        setHeader(last);
    }

    /**
     * 执行过滤
     *
     * @param context 过滤上下文
     */
    public C doFilter(C context) {
        LogMessage lm = LogMessages.create(config.getName(), "doFilter")
                .append("context", context);

        try {
            header.invoke(context);

            if (log.isInfoEnabled()) {
                log.info(lm.success().toString());
            }

            return context;
        } catch (Exception e) {
            LogService.logForError(log, lm, e);
            throw e;
        }
    }

    protected void setHeader(FilterInvoker<C> header) {
        this.header = header;
    }

    @Data
    @Builder
    public static class Config {
        @Builder.Default
        private String name = FilterChain.class.getSimpleName();

        @Builder.Default
        private List<? extends Filter<?>> filters = Collections.emptyList();

        @Builder.Default
        private List<Class<?>> filterClasses = Collections.emptyList();

        private List<? extends FilterInterceptor<?, ?>> interceptors;

        public List<? extends Filter<?>> filtersOfClasses() {
            return filterClasses.stream()
                    .map(it -> (Filter<?>) BeanProviders.getInstance().getBean(it))
                    .collect(Collectors.toList());
        }

        public List<? extends Filter<?>> filtersWithClasses() {
            if (!filters.isEmpty()) {
                return filters;
            }

            return filtersOfClasses();
        }
    }
}