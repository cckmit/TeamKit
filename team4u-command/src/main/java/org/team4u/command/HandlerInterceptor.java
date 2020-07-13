package org.team4u.command;


import org.team4u.base.filter.FilterInterceptor;

/**
 * 处理拦截器
 *
 * @author jay.wu
 */
public interface HandlerInterceptor extends FilterInterceptor<Handler.Context, Handler> {
}