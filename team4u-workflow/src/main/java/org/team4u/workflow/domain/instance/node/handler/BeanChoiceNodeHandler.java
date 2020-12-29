package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.ArrayUtil;
import org.team4u.workflow.domain.definition.node.BeanChoiceNode;
import org.team4u.workflow.domain.instance.exception.ProcessNodeUnexpectedException;

/**
 * 基于bean选择节点处理器
 *
 * @author jay.wu
 */
public class BeanChoiceNodeHandler implements ProcessNodeHandler {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanChoiceNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        BeanChoiceNode node = context.getNode();

        processBeanHandlers.getBean(node.getBeanName()).handle(context);

        String nextNodeId = context.ext(ProcessBeanHandler.EXT_NEXT_NODE_ID);

        if (!ArrayUtil.contains(node.getNextNodeIds(), nextNodeId)) {
            throw new ProcessNodeUnexpectedException(nextNodeId);
        }

        return nextNodeId;
    }

    @Override
    public String id() {
        return BeanChoiceNode.class.getName();
    }
}