package org.team4u.command.domain.executor;


import org.team4u.command.domain.config.CommandConfig;

public interface CommandExecutor {

    CommandResponse execute(CommandConfig config, CommandRequest request);
}