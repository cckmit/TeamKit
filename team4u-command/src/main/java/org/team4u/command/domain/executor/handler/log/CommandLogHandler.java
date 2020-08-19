package org.team4u.command.domain.executor.handler.log;


import org.team4u.command.domain.executor.handler.CommandHandler;

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
        CommandLog commandLog = commandLogRepository.logOf(context.getCommandId());

        if (commandLog == null) {
            commandLog = newCommandLog(context)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
        } else {
            commandLog.setResponse(context.getResponse())
                    .setUpdateTime(new Date());
        }

        commandLog.setDurationMillisecond(
                commandLog.getUpdateTime().getTime() - commandLog.getCreateTime().getTime()
        );
        commandLogRepository.save(commandLog);
    }

    protected CommandLog newCommandLog(Context context) {
        return new CommandLog(context.getRequest());
    }
}