package org.team4u.workflow.application.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * 开始流程实例命令
 *
 * @author jay.wu
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StartProcessInstanceCommand extends AbstractHandleProcessInstanceCommand {
    /**
     * 流程动作标识
     */
    private String actionId;
    /**
     * 附加信息
     */
    private Map<String, Object> ext;
}
