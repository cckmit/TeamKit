package org.team4u.workflow.domain.instance.node.handler;

/**
 * bean动作选择器
 *
 * @author jay.wu
 */
public abstract class ProcessBeanActionChoicer implements ProcessBeanHandler {

    @Override
    public void handle(ProcessNodeHandlerContext context) {
        context.ext(EXT_CHOICE_ACTION_ID, choiceActionId(context));
    }

    /**
     * 选择最终动作标识
     *
     * @param context 上下文
     * @return 动作标识
     */
    protected abstract String choiceActionId(ProcessNodeHandlerContext context);
}