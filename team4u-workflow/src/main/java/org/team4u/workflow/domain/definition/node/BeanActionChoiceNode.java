package org.team4u.workflow.domain.definition.node;

import java.util.List;

/**
 * 动作选择器节点
 *
 * @author jay.wu
 */
public class BeanActionChoiceNode extends ActionChoiceNode {
    /**
     * bean名称
     */
    private final String beanName;
    /**
     * bean配置
     */
    private final String beanConfig;

    public BeanActionChoiceNode(String nodeId,
                                String nodeName,
                                List<ActionNode> actionNodes,
                                String beanName) {
        this(nodeId, nodeName, actionNodes, beanName, null);
    }

    public BeanActionChoiceNode(String nodeId,
                                String nodeName,
                                List<ActionNode> actionNodes,
                                String beanName,
                                String beanConfig) {
        super(nodeId, nodeName, actionNodes);
        this.beanName = beanName;
        this.beanConfig = beanConfig;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getBeanConfig() {
        return beanConfig;
    }
}