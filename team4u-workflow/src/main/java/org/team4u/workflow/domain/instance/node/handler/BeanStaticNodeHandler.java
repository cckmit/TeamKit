package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanStaticNode;

/**
 * bean节点处理器
 *
 * @author jay.wu
 */
public class BeanStaticNodeHandler extends AbstractStaticProcessNodeHandler<BeanStaticNode> {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanStaticNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    protected void internalHandle(ProcessNodeHandlerContext context) {
        BeanStaticNode node = node(context);
        processBeanHandlers.getBean(node.getBeanName()).handle(context);
    }
}