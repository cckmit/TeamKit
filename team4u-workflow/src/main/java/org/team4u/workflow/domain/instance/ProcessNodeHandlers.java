package org.team4u.workflow.domain.instance;

import cn.hutool.log.Log;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.exception.ProcessNodeNotExistException;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.List;
import java.util.ServiceLoader;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<String, ProcessNodeHandler> {

    private final Log log = Log.get();

    public ProcessNodeHandlers() {
        ServiceLoader<ProcessNodeHandler> serviceLoader = ServiceLoader.load(ProcessNodeHandler.class);

        for (ProcessNodeHandler handler : serviceLoader) {
            saveIdObject(handler);
        }
    }

    public ProcessNodeHandlers(List<ProcessNodeHandler> objects) {
        super(objects);
    }

    public void handle(ProcessNodeHandler.Context context) {
        if (context.getInstance() == null) {
            throw new ProcessInstanceNotExistException("context instance is null");
        }

        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "handle")
                .append("instance", context.getInstance().toString())
                .append("operatorId", context.getOperatorId())
                .append("action", context.getAction().toString())
                .append("startNode", context.getInstance().getCurrentNode())
                .append("definition", context.getDefinition())
                .append("ext", context.getExt());

        ProcessNode nextNode = context.getDefinition().processNodeOf(
                context.getInstance()
                        .getCurrentNode()
                        .getNextNodeId()
        );

        if (nextNode == null) {
            throw new ProcessNodeNotExistException(lm.fail("nextNode is null").toString());
        }

        while (nextNode != null) {
            lm.append("nextNode", nextNode);
            log.info(lm.success().toString());

            nextNode = context.getDefinition().processNodeOf(
                    availableObjectOfId(nextNode.getClass().getName())
                            .handle(context.setNode(nextNode))
            );
        }
    }
}