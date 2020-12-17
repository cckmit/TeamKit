package org.team4u.workflow.application;

import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.application.command.HandleProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.form.ProcessFormRepository;
import org.team4u.workflow.domain.instance.ProcessInstance;

/**
 * 流程表单抽象应用服务
 *
 * @author jay.wu
 */
public class ProcessFormAppService {

    private final ProcessAppService processAppService;

    private final ProcessFormRepository processFormRepository;

    public ProcessFormAppService(ProcessAppService processAppService,
                                 ProcessFormRepository processFormRepository) {
        this.processAppService = processAppService;
        this.processFormRepository = processFormRepository;
    }

    /**
     * 获取流程表单模型
     *
     * @param formId     流程表单标识
     * @param operatorId 当前处理人
     * @return 流程表单模型
     */
    public ProcessFormModel formOf(String formId, String operatorId) {
        ProcessFormModel formModel = new ProcessFormModel();

        formModel.setForm(processFormRepository.formOf(formId));

        formModel.setInstance(processAppService.processInstanceOf(
                formModel.getForm().getFormHeader().getProcessInstanceId()
        ));

        formModel.setActions(processAppService.availableActionsOf(
                formModel.getInstance(), operatorId
        ));

        formModel.setEvents(processAppService.processNodeChangedEventsOf(
                formModel.getInstance().getProcessInstanceId()
        ));
        return formModel;
    }

    /**
     * 处理流程表单
     *
     * @param command 处理流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(HandleProcessFormCommand command) {
        ProcessInstance instance = processAppService.start(
                command.getStartProcessInstanceCommand()
        );

        if (shouldSaveProcessForm(actionOf(
                instance,
                command.getStartProcessInstanceCommand()
        ))) {
            processFormRepository.save(command.getProcessForm());
        }
    }

    private ProcessAction actionOf(ProcessInstance instance,
                                   StartProcessInstanceCommand command) {
        ProcessDefinition definition = processAppService.processDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );

        return definition.actionOf(command.getActionId());
    }

    /**
     * 判断是否需要保存流程表单
     * <p>
     * 默认只有编辑权限的动作才需要保存
     *
     * @param action 当前动作
     */
    protected boolean shouldSaveProcessForm(ProcessAction action) {
        return action.hasPermission(ProcessAction.Permission.EDIT.name());
    }
}