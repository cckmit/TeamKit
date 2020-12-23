package org.team4u.workflow.domain.definition.node;

import java.util.Map;

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
    private final Map<String, Object> beanConfig;

    public BeanNode(String nodeId,
                    String nodeName,
                    String beanName,
                    String nextNodeId) {
        this(nodeId, nodeName, beanName, null, nextNodeId);
    }

    public BeanNode(String nodeId,
                    String nodeName,
                    String beanName,
                    Map<String, Object> beanConfig,
                    String nextNodeId) {
        super(nodeId, nodeName, nextNodeId);
        this.beanName = beanName;
        this.beanConfig = beanConfig;
    }

    public String getBeanName() {
        return beanName;
    }

    public Map<String, Object> getBeanConfig() {
        return beanConfig;
    }
}