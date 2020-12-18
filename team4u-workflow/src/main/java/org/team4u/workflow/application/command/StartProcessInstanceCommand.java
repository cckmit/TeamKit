package org.team4u.workflow.application.command;

/**
 * 开始流程实例命令
 *
 * @author jay.wu
 */
public class StartProcessInstanceCommand extends HandleProcessInstanceCommand {
    /**
     * 流程实例标识
     */
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public StartProcessInstanceCommand setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
}
