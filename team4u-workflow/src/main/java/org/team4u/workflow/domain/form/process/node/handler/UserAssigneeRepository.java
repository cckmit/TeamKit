package org.team4u.workflow.domain.form.process.node.handler;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

import java.util.List;

/**
 * 指定用户处理人资源库
 *
 * @author jay.wu
 */
public class UserAssigneeRepository implements AssigneeRepository {

    @Override
    public List<String> assigneesOf(ProcessNodeHandlerContext context, AssigneeStaticNode node) {
        return StrUtil.splitTrim(node.getRuleExpression(), ",");
    }

    @Override
    public String id() {
        return AssigneeStaticNode.RULE_TYPE_USER;
    }
}