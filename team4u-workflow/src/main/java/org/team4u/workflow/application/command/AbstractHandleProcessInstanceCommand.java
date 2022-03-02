package org.team4u.workflow.application.command;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 处理流程实例命令
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
public abstract class AbstractHandleProcessInstanceCommand {
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 当前处理人
     */
    private String operatorId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 流程实例明细
     */
    private Object processInstanceDetail;
}