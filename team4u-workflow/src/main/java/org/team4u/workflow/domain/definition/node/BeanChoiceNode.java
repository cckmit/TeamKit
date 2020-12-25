package org.team4u.workflow.domain.definition.node;

/**
 * 基于bean的选择节点
 *
 * @author jay.wu
 */
public class BeanChoiceNode extends ChoiceNode {

    /**
     * bean名称
     */
    private final String beanName;
    /**
     * bean配置
     */
    private final String beanConfig;

    public BeanChoiceNode(String nodeId,
                          String nodeName,
                          String beanName) {
        this(nodeId, nodeName, beanName, null);
    }

    public BeanChoiceNode(String nodeId,
                          String nodeName,
                          String beanName,
                          String beanConfig) {
        super(nodeId, nodeName);
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