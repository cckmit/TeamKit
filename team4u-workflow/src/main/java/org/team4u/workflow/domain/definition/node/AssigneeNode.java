package org.team4u.workflow.domain.definition.node;

/**
 * 处理人节点
 *
 * @author jay.wu
 */
public class AssigneeNode extends TransientNode {

    public static final String RULE_TYPE_ROLE = "role";
    public static final String RULE_TYPE_USER = "user";

    /**
     * 流程人规则类型
     */
    private String ruleType;
    /**
     * 流程人规则表达式
     */
    private String ruleExpression;

    public String getRuleType() {
        return ruleType;
    }

    public AssigneeNode setRuleType(String ruleType) {
        this.ruleType = ruleType;
        return this;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }

    public AssigneeNode setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
        return this;
    }
}