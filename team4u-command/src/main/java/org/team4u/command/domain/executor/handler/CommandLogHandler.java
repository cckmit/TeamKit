package org.team4u.command.domain.executor.handler;


import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.CommandLog;
import org.team4u.command.domain.executor.CommandLogRepository;

public class CommandLogHandler implements CommandHandler {

    private final CommandLogRepository commandLogRepository;

    public CommandLogHandler(CommandLogRepository commandLogRepository) {
        this.commandLogRepository = commandLogRepository;
    }

    @Override
    public void handle(Context context) {
        CommandLog commandLog = new CommandLog(context.getRequest());
        commandLog.setResponse(context.getResponse());
        commandLogRepository.save(commandLog);
    }
}