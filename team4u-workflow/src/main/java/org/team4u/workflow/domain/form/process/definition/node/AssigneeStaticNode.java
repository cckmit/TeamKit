package org.team4u.workflow.domain.form.process.definition.node;

import org.team4u.workflow.domain.definition.node.StaticNode;

/**
 * 处理人节点
 *
 * @author jay.wu
 */
public class AssigneeStaticNode extends StaticNode {
    /**
     * 自定义处理人标识
     */
    public static final String RULE_TYPE_CUSTOM = "CUSTOM";
    /**
     * 指定用户标识
     */
    public static final String RULE_TYPE_USER = "USER";

    /**
     * 流程处理人类型
     */
    private final String ruleType;
    /**
     * 流程处理人表达式
     */
    private final String ruleExpression;

    public AssigneeStaticNode(String nodeId,
                              String nodeName,
                              String nextNodeId,
                              String ruleType,
                              String ruleExpression) {
        super(nodeId, nodeName, nextNodeId);
        this.ruleType = ruleType;
        this.ruleExpression = ruleExpression;
    }

    public String getRuleType() {
        return ruleType;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }
}