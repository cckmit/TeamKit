package org.team4u.command.infrastructure.executor.liteflow;

import com.yomahub.liteflow.core.FlowExecutor;
import org.team4u.base.error.NestedException;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Liteflow的命令执行器
 * <p>
 * https://yomahub.com/liteflow
 *
 * @author jay.wu
 */
public class LiteflowCommandExecutor extends IdObjectService<String, CommandRoutesBuilder> implements CommandExecutor {

    private final Map<String, FlowExecutor> flowMap = new HashMap<>();

    public LiteflowCommandExecutor() {
        super(CommandRoutesBuilder.class);

        initFlow();
    }

    public LiteflowCommandExecutor(List<CommandRoutesBuilder> objects) {
        super(objects);

        initFlow();
    }

    private void initFlow() {
        for (CommandRoutesBuilder builder : idObjects()) {
            initFlowExecutor(builder);
        }
    }

    public void saveAndInitFlow(CommandRoutesBuilder builder) {
        saveIdObject(builder);
        initFlowExecutor(builder);
    }

    private void initFlowExecutor(CommandRoutesBuilder builder) {
        builder.registerNodes();

        FlowExecutor executor = new FlowExecutor();
        executor.setRulePath(builder.configure());
        executor.init();

        flowMap.put(builder.id(), executor);
    }

    @Override
    public Object execute(String commandId, CommandConfig config, Object request) {
        FlowExecutor executor = flowMap.get(commandId);
        if (executor == null) {
            throw new SystemDataNotExistException("commandId=" + commandId);
        }

        CommandHandler.Context handlerContext = new CommandHandler.Context(commandId, config, request);
        try {
            executor.execute("main", handlerContext);
        } catch (Exception e) {
            throw new NestedException(e);
        }

        return handlerContext.getResponse();
    }
}