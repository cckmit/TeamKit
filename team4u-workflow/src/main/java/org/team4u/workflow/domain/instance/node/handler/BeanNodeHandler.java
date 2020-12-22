package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanNode;
import org.team4u.workflow.domain.instance.exception.ProcessBeanHandlerNotExistException;

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
        getBean(node.getBeanName()).handle(context);
    }

    private ProcessBeanHandler getBean(String beanName) {
        ProcessBeanHandler handler = processBeanHandlers.objectOfId(beanName);

        if (handler == null) {
            throw new ProcessBeanHandlerNotExistException(beanName);
        }

        return handler;
    }

    @Override
    public String id() {
        return BeanNode.class.getName();
    }
}