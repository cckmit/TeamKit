package org.team4u.command.application;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;

/**
 * 命令应用服务
 *
 * @author jay.wu
 */
public class CommandAppService {

    private final CommandExecutor commandExecutor;
    private final CommandConfigRepository commandConfigRepository;

    public CommandAppService(CommandExecutor commandExecutor,
                             CommandConfigRepository commandConfigRepository) {
        this.commandExecutor = commandExecutor;
        this.commandConfigRepository = commandConfigRepository;
    }

    /**
     * 执行命令
     *
     * @param request 命令请求
     * @return 命令响应
     */
    public <Response> Response execute(String commandId, Object request) {
        //noinspection unchecked
        return (Response) commandExecutor.execute(commandConfigOf(commandId), request);
    }

    /**
     * 获取命令配置
     *
     * @param commandId 命令标识
     * @return 命令配置
     */
    public CommandConfig commandConfigOf(String commandId) {
        return commandConfigRepository.configOf(commandId);
    }
}