package org.team4u.base.filter;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.bean.ServiceLoaderUtil;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤器服务
 *
 * @author jay.wu
 */
public class FilterService<C> {

    private final Log log = LogFactory.get();

    private FilterChain<C> filterChain;

    public FilterService() {
    }

    public FilterService(Filter<C>... filters) {
        this(Arrays.asList(filters));
    }

    public FilterService(Class<? extends Filter<C>> filterClass) {
        this(ServiceLoaderUtil.loadAvailableList(filterClass));
    }

    public FilterService(List<? extends Filter<C>> filters) {
        setFilterChain(FilterChain.create(filters));
    }

    /**
     * 开始处理
     *
     * @param context 上下文
     * @return context 上下文
     */
    public C doFilter(C context) {
        LogMessage lm = LogMessages.create(this.getClass().getSimpleName(), "doFilter")
                .append("context", context);
        try {
            filterChain.doFilter(context);

            if (log.isInfoEnabled()) {
                log.info(lm.success().toString());
            }

            return context;
        } catch (Exception e) {
            LogService.logForError(log, lm, e);
            throw e;
        }
    }

    public void setFilterChain(FilterChain<C> filterChain) {
        this.filterChain = filterChain;

        log.info(LogMessage.create(this.getClass().getSimpleName(), "create")
                .success()
                .append("filters",
                        filterChain.filters().stream()
                                .map(it -> it.getClass().getSimpleName())
                                .collect(Collectors.toList()))
                .toString());
    }

    public FilterChain<C> getFilterChain() {
        return filterChain;
    }
}