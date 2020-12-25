package org.team4u.workflow.domain.definition.node;

/**
 * bean节点
 *
 * @author jay.wu
 */
public class BeanNode extends StaticNode {
    /**
     * bean名称
     */
    private final String beanName;
    /**
     * bean配置
     */
    private final String beanConfig;

    public BeanNode(String nodeId,
                    String nodeName,
                    String beanName,
                    String nextNodeId) {
        this(nodeId, nodeName, beanName, null, nextNodeId);
    }

    public BeanNode(String nodeId,
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