package org.team4u.workflow.domain.instance.node.handler.bean;

import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.workflow.domain.instance.node.handler.BeanActionChoiceNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.BeanChoiceNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

/**
 * 流程bean处理器
 *
 * @author jay.wu
 */
public interface ProcessBeanHandler extends StringIdPolicy {

    /**
     * 扩展属性key：下一个节点标识
     *
     * @see BeanChoiceNodeHandler
     */
    String EXT_NEXT_NODE_ID = "nextNodeId";
    /**
     * 扩展属性key：选中动作标识
     *
     * @see BeanActionChoiceNodeHandler
     */
    String EXT_CHOICE_ACTION_ID = "choiceActionId";

    /**
     * 处理流程
     *
     * @param context 上下文
     */
    void handle(ProcessNodeHandlerContext context);
}