package org.team4u.command.application;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.ControlException;
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
    public <Response> Response execute(String commandId, Object request) {
        return execute(commandId, commandId, request);
    }

    /**
     * 执行命令
     *
     * @param request  命令请求
     * @param configId 配置标识
     * @return 命令响应
     */
    @SuppressWarnings("unchecked")
    public <Response> Response execute(String commandId, String configId, Object request) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "execute")
                .append("commandId", commandId)
                .append("request", request);
        Response response = null;
        try {
            response = (Response) commandExecutor.execute(
                    commandId,
                    commandConfigOf(configId),
                    request
            );

            log.info(lm.success()
                    .append("response", response)
                    .toString());
            return response;
        } catch (ControlException e) {
            log.info(lm.success()
                    .append("response", response)
                    .append("stop", e.getMessage())
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

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public CommandConfigRepository getCommandConfigRepository() {
        return commandConfigRepository;
    }
}