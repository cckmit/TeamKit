package org.team4u.workflow.domain.instance;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.List;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<String, ProcessNodeHandler> {

    public ProcessNodeHandlers() {
    }

    public ProcessNodeHandlers(List<ProcessNodeHandler> objects) {
        super(objects);
    }

    public void handle(ProcessNodeHandler.Context context) {
        ProcessNode nextNode = context.getDefinition().processNodeOf(
                context.getInstance()
                        .getCurrentNode()
                        .getNextNodeId()
        );

        if (nextNode == null) {
            throw new SystemDataNotExistException(String.format(
                    "NextNode|processInstanceId=%s|currentNodeId=%s",
                    context.getInstance().getProcessInstanceId(),
                    context.getInstance().getCurrentNode().getNodeId()
            ));
        }

        while (nextNode != null) {
            nextNode = availableObjectOfId(nextNode.getNodeId()).handle(context.setNode(nextNode));
        }
    }
}