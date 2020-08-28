package org.team4u.command.application;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;

/**
 * 命令应用服务
 *
 * @author jay.wu
 */
public class CommandAppService {

    private final Log log = LogFactory.get();

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
    @SuppressWarnings("unchecked")
    public <Response> Response execute(String commandId, Object request) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "execute")
                .append("commandId", commandId)
                .append("request", request);
        try {
            Response response = (Response) commandExecutor.execute(
                    commandId,
                    commandConfigOf(commandId),
                    request
            );

            log.info(lm.success()
                    .append("response", response)
                    .toString());
            return response;
        } catch (Exception e) {
            LogService.logForError(log, lm, e);
            throw e;
        }
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