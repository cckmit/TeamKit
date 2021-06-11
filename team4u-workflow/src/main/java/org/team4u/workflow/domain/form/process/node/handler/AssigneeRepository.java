package org.team4u.workflow.domain.form.process.node.handler;

import org.team4u.base.lang.IdObject;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import java.util.List;

/**
 * 处理人资源库
 *
 * @author jay.wu
 */
public interface AssigneeRepository extends IdObject<String> {

    /**
     * 获取处理人标识集合
     *
     * @param context 流程上下文
     * @param node    处理人节点
     * @return 处理人标识集合
     */
    List<String> assigneesOf(ProcessNodeHandlerContext context, AssigneeStaticNode node);
}