package org.team4u.command.infrastructure.executor.liteflow;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.property.LiteflowConfig;
import org.team4u.base.error.NestedException;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.registrar.PolicyRegistrar;
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
public class LiteFlowCommandExecutor extends PolicyRegistrar<String, CommandRoutesBuilder> implements CommandExecutor {

    private final Map<String, FlowExecutor> flowMap = new HashMap<>();

    public LiteFlowCommandExecutor() {
        initFlow();
    }

    public LiteFlowCommandExecutor(List<CommandRoutesBuilder> objects) {
        super(objects);

        initFlow();
    }

    private void initFlow() {
        for (CommandRoutesBuilder builder : policies()) {
            initFlowExecutor(builder);
        }
    }

    public void saveAndInitFlow(CommandRoutesBuilder builder) {
        registerPolicy(builder);
        initFlowExecutor(builder);
    }

    private void initFlowExecutor(CommandRoutesBuilder builder) {
        builder.registerNodes();

        FlowExecutor executor = new FlowExecutor();
        LiteflowConfig liteflowConfig = new LiteflowConfig();
        liteflowConfig.setRuleSource(StrUtil.join(",", builder.configure()));
        executor.setLiteflowConfig(liteflowConfig);
        executor.init();

        flowMap.put(builder.id(), executor);
    }

    @Override
    public Object execute(CommandHandler.Context context) {
        FlowExecutor executor = flowMap.get(context.getCommandId());
        if (executor == null) {
            throw new SystemDataNotExistException("commandId=" + context.getCommandId());
        }

        try {
            executor.execute("main", context);
        } catch (Exception e) {
            throw new NestedException(e);
        }

        return context.getResponse();
    }
}