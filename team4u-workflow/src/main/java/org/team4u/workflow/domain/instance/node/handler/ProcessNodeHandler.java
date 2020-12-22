package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObject;

/**
 * 流程节点处理器
 *
 * @author jay.wu
 */
public interface ProcessNodeHandler extends IdObject<String> {

    /**
     * 处理节点
     *
     * @param context 上下文
     * @return 下一个流程节点标识
     */
    String handle(ProcessNodeHandlerContext context);


}