package org.team4u.command.infrastructure.executor.filter;

import org.team4u.base.filter.v2.Filter;
import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 基于过滤器的命令处理器
 *
 * @author jay.wu
 */
public interface CommandFilter extends Filter<CommandHandler.Context> {
}