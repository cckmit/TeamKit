package org.team4u.workflow.domain.emulator;

import org.team4u.base.config.IdentifiedConfig;

import java.util.List;

/**
 * 流程模拟器脚本
 *
 * @author jay.wu
 */
public class ProcessEmulatorScript extends IdentifiedConfig {

    private String processDefinitionId;

    private List<ProcessEmulatorScriptStep> steps;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public List<ProcessEmulatorScriptStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ProcessEmulatorScriptStep> steps) {
        this.steps = steps;
    }
}