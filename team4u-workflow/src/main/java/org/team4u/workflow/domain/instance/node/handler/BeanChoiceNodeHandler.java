package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.ArrayUtil;
import org.team4u.workflow.domain.definition.node.BeanChoiceNode;
import org.team4u.workflow.domain.instance.exception.ProcessNodeUnexpectedException;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandler;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

/**
 * 基于bean选择节点处理器
 *
 * @author jay.wu
 */
public class BeanChoiceNodeHandler extends AbstractProcessNodeHandler<BeanChoiceNode> {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanChoiceNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        BeanChoiceNode node = node(context);

        processBeanHandlers.getBean(node.getBeanName()).handle(context);

        String nextNodeId = context.ext(ProcessBeanHandler.EXT_NEXT_NODE_ID);

        if (!ArrayUtil.contains(node.getNextNodeIds(), nextNodeId)) {
            throw new ProcessNodeUnexpectedException(nextNodeId);
        }

        return nextNodeId;
    }
}