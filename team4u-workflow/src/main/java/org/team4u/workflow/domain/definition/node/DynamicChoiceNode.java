package org.team4u.workflow.domain.definition.node;

import org.team4u.selector.domain.selector.SelectorConfig;

/**
 * 动态选择器节点
 *
 * @author jay.wu
 */
public class DynamicChoiceNode extends ChoiceNode implements TransientNode {

    /**
     * 选择器配置
     */
    private final SelectorConfig rule;

    public DynamicChoiceNode(String nodeId,
                             String nodeName,
                             SelectorConfig rule) {
        super(nodeId, nodeName);
        rule.setConfigId(getNodeId());
        this.rule = rule;
    }

    public SelectorConfig getRule() {
        return rule;
    }
}