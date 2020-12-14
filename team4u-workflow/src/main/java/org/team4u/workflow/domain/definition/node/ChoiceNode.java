package org.team4u.workflow.domain.definition.node;

/**
 * 选择器节点
 *
 * @author jay.wu
 */
public class ChoiceNode extends TransientNode {
    /**
     * 选择器类型
     */
    private String ruleType;
    /**
     * 选择器规则表达式
     */
    private String ruleExpression;

    public String getRuleType() {
        return ruleType;
    }

    public ChoiceNode setRuleType(String ruleType) {
        this.ruleType = ruleType;
        return this;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }

    public ChoiceNode setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
        return this;
    }
}