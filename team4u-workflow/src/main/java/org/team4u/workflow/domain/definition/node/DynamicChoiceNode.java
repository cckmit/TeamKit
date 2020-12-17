package org.team4u.workflow.domain.definition.node;

import org.team4u.selector.domain.config.entity.SelectorConfig;

/**
 * 动态选择器节点
 *
 * @author jay.wu
 */
public class DynamicChoiceNode extends ChoiceNode {

    /**
     * 选择器配置
     */
    private final SelectorConfig rule;

    public DynamicChoiceNode(String nodeId,
                             String nodeName,
                             SelectorConfig rule) {
        super(nodeId, nodeName);
        this.rule = rule.setId(getNodeId());
    }

    public SelectorConfig getRule() {
        return rule;
    }
}