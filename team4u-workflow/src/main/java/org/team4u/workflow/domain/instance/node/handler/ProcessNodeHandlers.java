package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObjectService;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.TransientNode;

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
        if (context.getInstance().getCurrentNode() == null) {
            context.setNode(context.getDefinition().rootProcessNode());
        } else {
            context.setNode(context.getInstance().getCurrentNode());
        }

        ProcessNode nextNode = availableObjectOfId(context.getNode().getNodeId()).handle(context);
        while (nextNode instanceof TransientNode) {
            nextNode = availableObjectOfId(context.getNode().getNodeId()).handle(context);
        }
    }
}