package org.team4u.command.domain.executor.handler.log;

/**
 * 命令日志资源库
 *
 * @author jay.wu
 */
public interface CommandLogRepository {

    /**
     * 获取命令日志
     *
     * @param commandLogId 日志标识
     * @return 命令日志
     */
    CommandLog logOf(String commandLogId);

    /**
     * 保存命令日志
     *
     * @param commandLog 命令日志
     */
    void save(CommandLog commandLog);
}