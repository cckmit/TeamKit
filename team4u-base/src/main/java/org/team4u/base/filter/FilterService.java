package org.team4u.base.filter;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * 过滤器服务
 *
 * @author jay.wu
 */
public class FilterService<C> {

    private final Log log = LogFactory.get();

    private FilterChain<C> contextFilterChain;

    public FilterService() {
    }

    public FilterService(Class<? extends Filter<C>> filterClass) {
        ServiceLoader<? extends Filter<C>> serviceLoader = ServiceLoader.load(filterClass);

        List<Filter<C>> filters = new ArrayList<>();
        for (Filter<C> filter : serviceLoader) {
            filters.add(filter);
        }

        buildFilterChainByFilters(filters);
    }

    public FilterService(List<? extends Filter<C>> filters) {
        buildFilterChainByFilters(filters);
    }

    /**
     * 根据过滤器集合构建过滤器责任链
     *
     * @param filters 过滤器集合
     */
    public void buildFilterChainByFilters(List<? extends Filter<C>> filters) {
        contextFilterChain = FilterChainFactory.buildFilterChain(filters);

        log.info(LogMessage.create(this.getClass().getSimpleName(), "initFilters")
                .success()
                .append("filters",
                        filters.stream()
                                .map(it -> it.getClass().getSimpleName())
                                .collect(Collectors.toList()))
                .toString());
    }

    /**
     * 开始处理
     *
     * @param context 上下文
     */
    public void doFilter(C context) {
        LogMessage lm = LogMessages.create(this.getClass().getSimpleName(), "doFilter")
                .append("context", context);
        try {
            contextFilterChain.doFilter(context);
            log.info(lm.success().toString());
        } catch (Exception e) {
            LogService.logForError(log, lm, e);
            throw e;
        }
    }
}