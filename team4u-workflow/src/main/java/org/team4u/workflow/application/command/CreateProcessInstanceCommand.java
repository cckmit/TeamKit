package org.team4u.workflow.application.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 创建新流程实例命令
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateProcessInstanceCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 流程实例类型
     */
    private String processInstanceType;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程定义标识
     */
    private String processDefinitionId;
}