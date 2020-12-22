package org.team4u.workflow.domain.definition.node;

/**
 * bean节点
 *
 * @author jay.wu
 */
public class BeanNode extends StaticNode {

    /**
     * Bean名称
     */
    private final String beanName;

    public BeanNode(String nodeId,
                    String nodeName,
                    String beanName,
                    String nextNodeId) {
        super(nodeId, nodeName, nextNodeId);
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}