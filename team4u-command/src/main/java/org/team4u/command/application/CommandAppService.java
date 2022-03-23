package org.team4u.command.application;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.ControlException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessages;
import org.team4u.base.log.LogService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;

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
    public <Response> Response execute(String commandId, String configId, Object request) {
        return execute(new CommandHandler.Context(commandId, commandConfigOf(configId), request));
    }

    /**
     * 执行命令
     *
     * @param context    上下文
     * @param <Response> 命令响应
     * @return 命令响应
     */
    public <Response> Response execute(CommandHandler.Context context) {
        LogMessage lm = LogMessages.createWithMasker(
                this.getClass().getSimpleName(), "execute|" + context.getCommandId()
        ).append("request", context.getRequest());

        try {
            commandExecutor.execute(context);

            log.info(lm.success().append("response", context.getResponse()).toString());

            return context.getResponse();
        } catch (ControlException e) {
            log.info(lm.success()
                    .append("response", context.getResponse())
                    .append("stop", e.getMessage())
                    .toString());
            return context.getResponse();
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
        return commandConfigRepository.configOfId(commandId);
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public CommandConfigRepository getCommandConfigRepository() {
        return commandConfigRepository;
    }
}