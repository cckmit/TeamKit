package org.team4u.workflow.application;

import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormRepository;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.HashMap;
import java.util.Set;

/**
 * 流程表单抽象应用服务
 *
 * @author jay.wu
 */
public class ProcessFormAppService {

    private final ProcessAppService processAppService;

    private final ProcessFormRepository<?> processFormRepository;

    public ProcessFormAppService(ProcessAppService processAppService,
                                 ProcessFormRepository<?> processFormRepository) {
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
                formModel.getForm().getProcessInstanceId()
        ));

        if (!hasViewPermission(formModel.getInstance(), operatorId)) {
            throw new NoPermissionException("您没有权限查看请表单");
        }

        formModel.setActions(processAppService.availableActionsOf(
                formModel.getInstance(), operatorId
        ));

        formModel.setEvents(processAppService.processNodeChangedEventsOf(
                formModel.getInstance().getProcessInstanceId()
        ));
        return formModel;
    }

    private boolean hasViewPermission(ProcessInstance instance, String operatorId) {
        Set<String> permissions = processAppService.operatorPermissionsOf(instance, null, operatorId);
        return permissions.contains(ProcessAction.Permission.VIEW.name());
    }

    /**
     * 创建流程表单
     *
     * @param command 流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateProcessFormCommand command) {
        ProcessInstance instance = processAppService.create(command);

        if (shouldSaveProcessForm(actionOf(
                instance,
                command.getActionId()
        ))) {
            initCommandExt(command, command.getProcessForm());
            command.getProcessForm().setProcessInstanceId(instance.getProcessInstanceId());
            processFormRepository.save(command.getProcessForm());
        }
    }

    private void initCommandExt(AbstractHandleProcessInstanceCommand command, ProcessForm form) {
        if (command.getExt() == null) {
            command.setExt(new HashMap<>());
        }

        command.getExt().put("form", form);
    }

    /**
     * 处理流程表单
     *
     * @param command 流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void start(StartProcessFormCommand command) {
        ProcessForm form = processFormRepository.formOf(command.getProcessForm().getFormId());
        ProcessInstance instance = processAppService.start(
                StartProcessInstanceCommand.Builder
                        .newBuilder()
                        .withActionId(command.getActionId())
                        .withOperatorId(command.getOperatorId())
                        .withProcessInstanceId(form.getProcessInstanceId())
                        .withRemark(command.getRemark())
                        .withExt(command.getExt())
                        .build());

        if (shouldSaveProcessForm(actionOf(
                instance,
                command.getActionId()
        ))) {
            initCommandExt(command, command.getProcessForm());
            processFormRepository.save(command.getProcessForm());
        }
    }

    private ProcessAction actionOf(ProcessInstance instance,
                                   String actionId) {
        ProcessDefinition definition = processAppService.processDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );

        return definition.actionOf(actionId);
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