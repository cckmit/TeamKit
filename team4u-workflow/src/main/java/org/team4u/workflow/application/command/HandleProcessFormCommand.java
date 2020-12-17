package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.ProcessForm;

/**
 * 处理流程表单命令
 *
 * @author jay.wu
 */
public class HandleProcessFormCommand {

    private final ProcessForm processForm;
    private final StartProcessInstanceCommand startProcessInstanceCommand;

    public HandleProcessFormCommand(ProcessForm processForm,
                                    StartProcessInstanceCommand startProcessInstanceCommand) {
        this.processForm = processForm;
        this.startProcessInstanceCommand = startProcessInstanceCommand;
    }

    public StartProcessInstanceCommand getStartProcessInstanceCommand() {
        return startProcessInstanceCommand;
    }

    public ProcessForm getProcessForm() {
        return processForm;
    }
}