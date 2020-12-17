package org.team4u.workflow.application.command;

import org.team4u.workflow.domain.form.ProcessForm;

/**
 * 保存流程表单命令
 *
 * @author jay.wu
 */
public class HandleProcessFormCommand {

    private final StartProcessInstanceCommand startProcessInstanceCommand;
    private final ProcessForm processForm;

    public HandleProcessFormCommand(StartProcessInstanceCommand startProcessInstanceCommand,
                                    ProcessForm processForm) {
        this.startProcessInstanceCommand = startProcessInstanceCommand;
        this.processForm = processForm;
    }

    public StartProcessInstanceCommand getStartProcessInstanceCommand() {
        return startProcessInstanceCommand;
    }

    public ProcessForm getProcessForm() {
        return processForm;
    }
}