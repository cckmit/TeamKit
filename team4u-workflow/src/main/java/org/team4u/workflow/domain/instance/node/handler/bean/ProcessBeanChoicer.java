package org.team4u.workflow.domain.instance.node.handler.bean;

import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

/**
 * bean选择器
 *
 * @author jay.wu
 */
public abstract class ProcessBeanChoicer implements ProcessBeanHandler {

    @Override
    public void handle(ProcessNodeHandlerContext context) {
        context.ext(
                ProcessBeanHandler.EXT_NEXT_NODE_ID,
                choiceNextNodeId(context)
        );
    }

    /**
     * 选择下一节点标识
     *
     * @param context 上下文
     * @return 节点标识
     */
    protected abstract String choiceNextNodeId(ProcessNodeHandlerContext context);
}