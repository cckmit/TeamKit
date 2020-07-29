package org.team4u.command.application;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;

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
    public CommandResponse execute(CommandRequest request) {
        CommandConfig config = commandConfigRepository.configOf(request.getCommandId());
        return commandExecutor.execute(config, request);
    }
}