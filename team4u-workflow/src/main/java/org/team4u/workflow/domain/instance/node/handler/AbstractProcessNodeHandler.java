package org.team4u.workflow.domain.instance.node.handler;

import cn.hutool.core.util.ClassUtil;
import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 抽象节点处理器
 *
 * @author jay.wu
 */
public abstract class AbstractProcessNodeHandler<N extends ProcessNode> implements ProcessNodeHandler {

    /**
     * 获取当前处理节点
     *
     * @param context 上下文
     * @return 流程节点
     */
    public N node(ProcessNodeHandlerContext context) {
        return context.getNode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends ProcessNode> id() {
        return (Class<? extends ProcessNode>) ClassUtil.getTypeArgument(this.getClass());
    }
}