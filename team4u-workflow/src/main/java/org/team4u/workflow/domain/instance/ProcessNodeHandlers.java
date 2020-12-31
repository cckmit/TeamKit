package org.team4u.workflow.domain.instance;

import cn.hutool.log.Log;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.exception.TransientNodeException;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.definition.node.TransientNode;
import org.team4u.workflow.domain.instance.exception.ProcessInstanceCompletedException;
import org.team4u.workflow.domain.instance.exception.ProcessNodeHandlerNotExistException;
import org.team4u.workflow.domain.instance.node.handler.*;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<Class<? extends ProcessNode>, ProcessNodeHandler> {

    private final Log log = Log.get();

    private final ProcessBeanHandlers beanHandlers;

    public ProcessNodeHandlers() {
        super(ProcessNodeHandler.class);

        beanHandlers = new ProcessBeanHandlers();

        initProcessNodeHandler();
    }

    private void initProcessNodeHandler() {
        saveIdObject(new BeanStaticNodeHandler(beanHandlers));
        saveIdObject(new BeanChoiceNodeHandler(beanHandlers));
        saveIdObject(new BeanProcessingNodeHandler(beanHandlers));
        saveIdObject(new BeanActionChoiceNodeHandler(beanHandlers));
    }

    /**
     * 获取流程bean节点处理器服务
     */
    public ProcessBeanHandlers beanHandlers() {
        return beanHandlers;
    }

    /**
     * 处理节点
     */
    public void handle(ProcessNodeHandlerContext context) throws
            ProcessInstanceCompletedException,
            ProcessNodeHandlerNotExistException,
            TransientNodeException {
        LogMessage lm = logMessageOf(context);

        if (context.getInstance().isCompleted()) {
            throw new ProcessInstanceCompletedException(lm.fail().toString());
        }

        ProcessNode nextNode = processNodeToHandle(context);

        while (nextNode != null) {
            log.info(lm.success().append("nextNode", nextNode).toString());

            ProcessNode preNode = nextNode;

            nextNode = handleNextNode(context, nextNode);

            checkLastNode(lm, preNode, nextNode);
        }
    }

    private LogMessage logMessageOf(ProcessNodeHandlerContext context) {
        return LogMessage.create(this.getClass().getSimpleName(), "handle")
                .append("instance", context.getInstance().toString())
                .append("operatorId", context.getOperatorId())
                .append("action", context.getAction().toString())
                .append("startNode", context.getInstance().getCurrentNode().getNodeId())
                .append("definition", context.getDefinition())
                .append("ext", context.getExt());
    }

    private void checkLastNode(LogMessage lm,
                               ProcessNode preNode,
                               ProcessNode nextNode) {
        if (nextNode != null) {
            return;
        }

        // 最后一个节点必须为静态节点
        if (preNode instanceof TransientNode) {
            throw new TransientNodeException(
                    lm.fail(preNode.getNodeId() + " is a TransientNode").toString()
            );
        }
    }

    private ProcessNode handleNextNode(ProcessNodeHandlerContext context, ProcessNode currentNode) {
        context.setNode(currentNode);

        String nextNodeId = processNodeHandlerOf(currentNode).handle(context);

        changeCurrentNodeTo(context);

        return context.getDefinition().processNodeOf(nextNodeId);
    }

    private ProcessNode processNodeToHandle(ProcessNodeHandlerContext context) {
        ProcessNode nextNode = context.getInstance().getCurrentNode();

        if (context.getInstance().getCurrentNode() instanceof StaticNode) {
            StaticNode currentNode = context.getInstance().getCurrentNode();
            nextNode = context.getDefinition().processNodeOf(currentNode.getNextNodeId());
        }

        return nextNode;
    }

    private ProcessNodeHandler processNodeHandlerOf(ProcessNode node) {
        try {
            return availableObjectOfId(node.getClass());
        } catch (SystemDataNotExistException e) {
            throw new ProcessNodeHandlerNotExistException(node.getClass().getName());
        }
    }

    protected void changeCurrentNodeTo(ProcessNodeHandlerContext context) {
        if (context.getNode() instanceof TransientNode) {
            return;
        }

        context.getInstance().changeCurrentNodeTo(
                context.getAction(),
                context.getNode(),
                context.getOperatorId(),
                context.getRemark()
        );
    }
}