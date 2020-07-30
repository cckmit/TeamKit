package org.team4u.command.domain.executor;


import org.team4u.command.domain.config.CommandConfig;

/**
 * 命令执行器
 *
 * @author jay.wu
 */
public interface CommandExecutor {

    /**
     * 执行命令
     *
     * @param commandId 命令标识
     * @param config    命令配置
     * @param request   命令请求
     * @return 命令响应
     */
    Object execute(String commandId, CommandConfig config, Object request);
}