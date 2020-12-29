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
    /**
     * 下一节点候选组
     */
    private final String[] nextNodeIds;

    public BeanChoiceNode(String nodeId,
                          String nodeName,
                          String beanName,
                          String[] nextNodeIds) {
        this(nodeId, nodeName, beanName, null, nextNodeIds);
    }

    public BeanChoiceNode(String nodeId,
                          String nodeName,
                          String beanName,
                          String beanConfig,
                          String[] nextNodeIds) {
        super(nodeId, nodeName);
        this.beanName = beanName;
        this.beanConfig = beanConfig;
        this.nextNodeIds = nextNodeIds;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getBeanConfig() {
        return beanConfig;
    }

    public String[] getNextNodeIds() {
        return nextNodeIds;
    }
}