package org.team4u.workflow.domain.definition.node;

/**
 * 处理人节点
 *
 * @author jay.wu
 */
public class AssigneeNode extends StaticNode {

    public static final String RULE_TYPE_ROLE = "role";
    public static final String RULE_TYPE_USER = "user";

    /**
     * 流程人规则类型
     */
    private final String ruleType;
    /**
     * 流程人规则表达式
     */
    private final String ruleExpression;

    public AssigneeNode(String nodeId,
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