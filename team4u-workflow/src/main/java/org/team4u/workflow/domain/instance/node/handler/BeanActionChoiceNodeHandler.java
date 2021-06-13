package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.workflow.domain.definition.node.BeanActionChoiceNode;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandler;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandlers;

/**
 * bean动作选择器节点处理器
 * <p>
 * ProcessBeanHandler需要设置Context的ext以指定执行动作标识
 * <p>
 * key=ProcessBeanHandler.EXT_CHOICE_ACTION_ID
 * value=执行动作标识
 *
 * @author jay.wu
 */
public class BeanActionChoiceNodeHandler extends AbstractActionChoiceNodeHandler<BeanActionChoiceNode> {

    private final ProcessBeanHandlers processBeanHandlers;

    public BeanActionChoiceNodeHandler(ProcessBeanHandlers processBeanHandlers) {
        this.processBeanHandlers = processBeanHandlers;
    }

    @Override
    public String handle(ProcessNodeHandlerContext context) {
        processBeanHandlers.getBean(node(context).getBeanName()).handle(context);

        String choiceActionId = context.ext(ProcessBeanHandler.EXT_CHOICE_ACTION_ID);
        context.setAction(
                context.getDefinition().availableActionOf(choiceActionId)
        );

        return super.handle(context);
    }
}