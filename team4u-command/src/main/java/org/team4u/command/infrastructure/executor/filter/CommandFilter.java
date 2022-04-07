package org.team4u.command.infrastructure.executor.filter;

import org.team4u.base.filter.v2.Filter;
import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 基于过滤器的命令处理器
 *
 * @author jay.wu
 */
public interface CommandFilter extends Filter<CommandHandler.Context> {
    /**
     * 跳过处理器
     * <p>
     * 不执行任何逻辑，跳过执行下一个处理器
     */
    CommandFilter SKIP_FILTER = context -> true;

    /**
     * 终止处理器
     * <p>
     * 不执行任何逻辑，终止后续执行
     */
    CommandFilter END_FILTER = context -> false;
}