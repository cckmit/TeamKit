package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanProcessingNode;

/**
 * bean处理节点处理器
 *
 * @author jay.wu
 */
public class BeanProcessingNodeHandler extends AbstractProcessNodeHandler<BeanProcessingNode> {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanProcessingNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        BeanProcessingNode node = node(context);
        processBeanHandlers.getBean(node.getBeanName()).handle(context);
        return node.getNextNodeId();
    }
}