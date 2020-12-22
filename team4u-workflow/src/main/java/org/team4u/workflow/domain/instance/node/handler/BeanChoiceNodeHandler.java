package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanChoiceNode;

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

        return context.ext(ProcessBeanHandler.EXT_NEXT_NODE_ID);
    }

    @Override
    public String id() {
        return BeanChoiceNode.class.getName();
    }
}