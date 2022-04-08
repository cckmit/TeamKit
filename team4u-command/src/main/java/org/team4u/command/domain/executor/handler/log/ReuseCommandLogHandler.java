package org.team4u.command.domain.executor.handler.log;

import org.team4u.command.domain.executor.ContextNames;

import java.util.Date;

/**
 * 可重用的命令日志处理器
 * <p>
 * 当调用方重复调用相同的命令时：
 * - 命令执行前，对日志预加载，若已存在日志则直接返回，否则进行插入
 * - 命令执行后，对日志进行更新
 *
 * @author jay.wu
 */
public class ReuseCommandLogHandler extends CommandLogHandler {

    private final CommandLogRepository commandLogRepository;

    public ReuseCommandLogHandler(CommandLogRepository commandLogRepository) {
        super(commandLogRepository);
        this.commandLogRepository = commandLogRepository;
    }

    @Override
    public void handle(Context context) {
        CommandLog commandLog = context.extraAttribute(ContextNames.COMMAND_LOG);

        // Context中存在日志，说明命令已执行完毕，需要进行更新
        boolean isNeedSave = commandLog != null;

        // 在Context中不存在，尝试查找历史数据
        if (commandLog == null) {
            commandLog = commandLogRepository.logOf(context.getCommandLogId());
        }

        // 日志不存在，需要初始化，后续进行插入
        if (commandLog == null) {
            isNeedSave = true;
            commandLog = newCommandLog(context).setCreateTime(new Date());
        }

        // 需要保存日志
        if (isNeedSave) {
            commandLog.setResponse(context.getResponse())
                    .setUpdateTime(new Date())
                    .setDurationMillisecond(
                            commandLog.getUpdateTime().getTime() - commandLog.getCreateTime().getTime()
                    );
            commandLogRepository.save(commandLog);

            commandLog.setSaveBefore(true);
        }

        context.setExtraAttribute(ContextNames.COMMAND_LOG, commandLog);
    }
}