package org.team4u.command.domain.executor;


import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 命令执行器
 *
 * @author jay.wu
 */
public interface CommandExecutor {

    /**
     * 执行命令
     *
     * @param context 上下文
     * @return 命令响应
     */
    Object execute(CommandHandler.Context context);
}