package org.team4u.workflow.domain.definition.node;

/**
 * bean处理节点
 *
 * @author jay.wu
 */
public class BeanProcessingNode extends InternalProcessingNode implements TransientNode {
    /**
     * bean名称
     */
    private final String beanName;
    /**
     * bean配置
     */
    private final String beanConfig;

    public BeanProcessingNode(String nodeId,
                              String nodeName,
                              String beanName,
                              String nextNodeId) {
        this(nodeId, nodeName, beanName, null, nextNodeId);
    }

    public BeanProcessingNode(String nodeId,
                              String nodeName,
                              String beanName,
                              String beanConfig,
                              String nextNodeId) {
        super(nodeId, nodeName, nextNodeId);
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