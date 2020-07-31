package org.team4u.command.domain.executor.handler;


import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.CommandLog;
import org.team4u.command.domain.executor.CommandLogRepository;

import java.util.Date;

/**
 * 命令日志处理器
 *
 * @author jay.wu
 */
public class CommandLogHandler implements CommandHandler {

    private final CommandLogRepository commandLogRepository;

    public CommandLogHandler(CommandLogRepository commandLogRepository) {
        this.commandLogRepository = commandLogRepository;
    }

    @Override
    public void handle(Context context) {
        CommandLog commandLog = newCommandLog(context.getRequest())
                .setUpdateTime(new Date());

        if (context.getResponse() == null) {
            commandLog.setCreateTime(new Date());
        } else {
            commandLog.setResponse(context.getResponse());
        }
        commandLogRepository.save(commandLog);
    }

    protected CommandLog newCommandLog(Context context) {
        return new CommandLog(context.getRequest());
    }
}