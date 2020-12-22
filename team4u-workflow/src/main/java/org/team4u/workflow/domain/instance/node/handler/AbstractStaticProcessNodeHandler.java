package org.team4u.workflow.domain.instance.node.handler;

/**
 * 抽象静态节点处理器
 *
 * @author jay.wu
 */
public abstract class AbstractStaticProcessNodeHandler implements ProcessNodeHandler {

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