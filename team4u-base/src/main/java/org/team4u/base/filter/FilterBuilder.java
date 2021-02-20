package org.team4u.base.filter;

import cn.hutool.core.lang.func.VoidFunc1;

/**
 * 过滤器构造器
 *
 * @author jay.wu
 */
public class FilterBuilder<T> {

    /**
     * 是否跳过下一个过滤器
     */
    private boolean skipNextFilter = false;
    /**
     * 错误时是否跳过下一个过滤器
     */
    private boolean skipOnError = true;
    /**
     * 实际处理者
     */
    private VoidFunc1<T> worker;

    public Filter<T> build() {
        return (context, nextFilterInvoker) -> {
            try {
                worker.callWithRuntimeException(context);
            } catch (Exception e) {
                if (isSkipOnError()) {
                    throw e;
                }
            }

            if (!isSkipNextFilter()) {
                nextFilterInvoker.invoke(context);
            }
        };
    }

    public boolean isSkipNextFilter() {
        return skipNextFilter;
    }

    public FilterBuilder<T> setSkipNextFilter(boolean skipNextFilter) {
        this.skipNextFilter = skipNextFilter;
        return this;
    }

    public boolean isSkipOnError() {
        return skipOnError;
    }

    public FilterBuilder<T> setSkipOnError(boolean skipOnError) {
        this.skipOnError = skipOnError;
        return this;
    }

    public VoidFunc1<T> getWorker() {
        return worker;
    }

    public FilterBuilder<T> setWorker(VoidFunc1<T> worker) {
        this.worker = worker;
        return this;
    }
}