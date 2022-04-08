package org.team4u.base.filter.v2;

import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.log.LogMessageContext;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.team4u.base.log.LogService.withInfoLog;

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
    private final FilterInvoker<C> header;

    private final FilterInterceptorService<C, F> interceptorService;

    private FilterChain(Config config) {
        this.config = config;
        this.interceptorService = new FilterInterceptorService<>(config.getInterceptors());
        this.header = initFilterChain();
    }

    public static <C, F extends Filter<C>> FilterChain<C, F> create(Config config) {
        return new FilterChain<>(config);
    }

    @SuppressWarnings("unchecked")
    private FilterInvoker<C> initFilterChain() {
        return withInfoLog(log, config.getName(), "initFilterChain", () -> {
            List<F> filters = (List<F>) config.filtersWithClasses();
            LogMessageContext.get().append("filters", config.dump(filters));

            final AtomicReference<FilterInvoker<C>> last = new AtomicReference<FilterInvoker<C>>(
                    FilterInvoker.EMPTY_INVOKER
            );
            reverse(filters, filter -> {
                FilterInvoker<C> next = last.get();
                last.set(context -> interceptorService.apply(context, next, filter));
            });

            return last.get();
        });
    }

    private void reverse(List<F> filters, VoidFunc1<F> worker) {
        for (int i = filters.size() - 1; i >= 0; i--) {
            worker.callWithRuntimeException(filters.get(i));
        }
    }

    /**
     * 执行过滤
     *
     * @param context 过滤上下文
     */
    public C doFilter(C context) {
        return withInfoLog(log, config.getName(), "doFilter", () -> {
            LogMessageContext.get().append("context", context);
            header.invoke(context);
            return context;
        });
    }

    @Data
    @Builder
    public static class Config {

        public static final String DEFAULT_NAME = FilterChain.class.getSimpleName();

        @Builder.Default
        private String name = DEFAULT_NAME;

        @Singular
        private List<? extends Filter<?>> filters;

        @Singular
        private List<Class<? extends Filter<?>>> filterClasses;

        @Singular
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

        public boolean isDefaultName() {
            return StrUtil.equals(getName(), DEFAULT_NAME);
        }

        public void setNameIfNotDefault(String name) {
            if (!isDefaultName()) {
                return;
            }

            setName(name);
        }


        public List<String> dump(List<? extends Filter<?>> filters) {
            return filters.stream()
                    .map(it -> ClassUtil.getShortClassName(it.getClass().getName()))
                    .collect(Collectors.toList());
        }
    }
}