package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObject;

/**
 * 流程bean处理器
 *
 * @author jay.wu
 */
public interface ProcessBeanHandler extends IdObject<String> {

    String NEXT_NODE_ID = "nextNodeId";

    /**
     * 处理流程
     *
     * @param context 上下文
     */
    void handle(ProcessNodeHandlerContext context);
}