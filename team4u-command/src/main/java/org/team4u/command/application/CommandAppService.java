package org.team4u.command.application;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;

public class CommandAppService {

    private final CommandExecutor commandExecutor;
    private final CommandConfigRepository commandConfigRepository;

    public CommandAppService(CommandExecutor commandExecutor,
                             CommandConfigRepository commandConfigRepository) {
        this.commandExecutor = commandExecutor;
        this.commandConfigRepository = commandConfigRepository;
    }

    public CommandResponse execute(CommandRequest request) {
        CommandConfig config = commandConfigRepository.configOf(request.getCommandId());
        return commandExecutor.execute(config, request);
    }
}