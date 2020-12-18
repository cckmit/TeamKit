package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.ProcessForm;

/**
 * 创建流程表单命令
 *
 * @author jay.wu
 */
public class CreateProcessFormCommand extends CreateProcessInstanceCommand {

    private ProcessForm processForm;

    public ProcessForm getProcessForm() {
        return processForm;
    }

    public void setProcessForm(ProcessForm processForm) {
        this.processForm = processForm;
    }
}