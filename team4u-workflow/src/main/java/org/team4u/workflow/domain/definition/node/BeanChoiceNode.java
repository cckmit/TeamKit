package org.team4u.workflow.domain.definition.node;

/**
 * 基于bean的选择节点
 *
 * @author jay.wu
 */
public class BeanChoiceNode extends ChoiceNode {

    /**
     * Bean名称
     */
    private final String beanName;

    public BeanChoiceNode(String nodeId,
                          String nodeName,
                          String beanName) {
        super(nodeId, nodeName);
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}