package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 抽象静态节点处理器
 *
 * @author jay.wu
 */
public abstract class AbstractStaticProcessNodeHandler<N extends ProcessNode>
        extends AbstractProcessNodeHandler<N> {

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        internalHandle(context);

        changeCurrentNodeTo(context);

        return null;
    }

    protected void changeCurrentNodeTo(ProcessNodeHandlerContext context) {
        context.getInstance().changeCurrentNodeTo(
                context.getAction(),
                context.getNode(),
                context.getOperatorId(),
                context.getRemark()
        );
    }

    /**
     * 内部处理流程
     *
     * @param context 上下文
     */
    protected abstract void internalHandle(ProcessNodeHandlerContext context);
}