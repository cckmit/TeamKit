package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanNode;

/**
 * bean节点处理器
 *
 * @author jay.wu
 */
public class BeanNodeHandler extends AbstractStaticProcessNodeHandler {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    protected void internalHandle(ProcessNodeHandlerContext context) {
        BeanNode node = context.getNode();
        processBeanHandlers.getBean(node.getBeanName()).handle(context);
    }

    @Override
    public String id() {
        return BeanNode.class.getName();
    }
}