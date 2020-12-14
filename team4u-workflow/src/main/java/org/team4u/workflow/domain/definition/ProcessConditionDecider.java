package org.team4u.workflow.domain.definition;

import org.team4u.base.lang.IdObject;

/**
 * 流程条件抉择器
 *
 * @author jay.wu
 */
public interface ProcessConditionDecider extends IdObject<String> {

    /**
     * 进行抉择
     *
     * @return 选定的流程节点
     */
    ProcessNode decide();
}