package org.team4u.workflow.domain.instance;

import cn.hutool.log.Log;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.exception.NotStaticNodeException;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.exception.ProcessInstanceCompletedException;
import org.team4u.workflow.domain.instance.exception.ProcessNodeHandlerNotExistException;
import org.team4u.workflow.domain.instance.node.handler.*;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<String, ProcessNodeHandler> {

    private final Log log = Log.get();

    private final ProcessBeanHandlers beanHandlers;

    public ProcessNodeHandlers() {
        super(ProcessNodeHandler.class);

        beanHandlers = new ProcessBeanHandlers();

        initProcessNodeHandler();
    }

    /**
     * 处理节点
     */
    public void handle(ProcessNodeHandlerContext context) throws
            ProcessInstanceCompletedException,
            ProcessNodeHandlerNotExistException,
            NotStaticNodeException {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "handle")
                .append("instance", context.getInstance().toString())
                .append("operatorId", context.getOperatorId())
                .append("action", context.getAction().toString())
                .append("startNode", context.getInstance().getCurrentNode().getNodeId())
                .append("definition", context.getDefinition())
                .append("ext", context.getExt());

        if (context.getInstance().isCompleted()) {
            throw new ProcessInstanceCompletedException(lm.fail().toString());
        }

        String nextNodeId = context.getInstance().getCurrentNode().getNextNodeId();
        ProcessNode nextNode = context.getDefinition().processNodeOf(nextNodeId);

        while (nextNode != null) {
            lm.append("nextNode", nextNode);
            log.info(lm.success().toString());

            ProcessNode preNode = nextNode;

            nextNode = context.getDefinition().processNodeOf(
                    processNodeHandlerOf(nextNode).handle(context.setNode(nextNode))
            );

            // 最后一个节点必须为静态节点
            if (nextNode == null && !(preNode instanceof StaticNode)) {
                throw new NotStaticNodeException(
                        lm.fail(preNode.getNodeId() + "is not staticNode").toString()
                );
            }
        }
    }

    /**
     * 获取流程bean节点处理器服务
     */
    public ProcessBeanHandlers beanHandlers() {
        return beanHandlers;
    }

    private ProcessNodeHandler processNodeHandlerOf(ProcessNode node) {
        try {
            return availableObjectOfId(node.getClass().getName());
        } catch (SystemDataNotExistException e) {
            throw new ProcessNodeHandlerNotExistException(node.getClass().getName());
        }
    }

    private void initProcessNodeHandler() {
        saveIdObject(new BeanNodeHandler(beanHandlers));
        saveIdObject(new BeanChoiceNodeHandler(beanHandlers));
        saveIdObject(new BeanProcessingNodeHandler(beanHandlers));
    }
}