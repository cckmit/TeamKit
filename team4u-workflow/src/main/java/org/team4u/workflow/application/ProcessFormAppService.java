package org.team4u.workflow.application;

import org.springframework.transaction.annotation.Transactional;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.form.ProcessFormContextKeys;
import org.team4u.workflow.domain.form.ProcessFormPermissionService;
import org.team4u.workflow.domain.form.ProcessFormRepository;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessInstance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程表单抽象应用服务
 *
 * @author jay.wu
 */
public class ProcessFormAppService {

    private final ProcessAppService processAppService;
    @SuppressWarnings("rawtypes")
    private final ProcessFormRepository processFormRepository;
    private final ProcessFormPermissionService processFormPermissionService;

    public ProcessFormAppService(ProcessAppService processAppService,
                                 ProcessFormRepository<?> processFormRepository,
                                 ProcessFormPermissionService processFormPermissionService) {
        this.processAppService = processAppService;
        this.processFormRepository = processFormRepository;
        this.processFormPermissionService = processFormPermissionService;
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

        if (!hasViewPermission(formModel.getForm(), formModel.getInstance(), operatorId)) {
            throw new NoPermissionException("您没有权限查看请表单");
        }

        formModel.setActions(availableActionsOf(formModel.getForm(), formModel.getInstance(), operatorId));

        formModel.setEvents(processAppService.processNodeChangedEventsOf(
                formModel.getInstance().getProcessInstanceId()
        ));
        return formModel;
    }

    /**
     * 创建流程表单
     *
     * @param command 流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateProcessFormCommand command) {
        initCommandExt(command, null, command.getProcessForm());
        ProcessInstance instance = processAppService.create(command);

        if (shouldSaveProcessForm(actionOf(instance, command.getActionId()))) {
            command.getProcessForm().setProcessInstanceId(instance.getProcessInstanceId());
            //noinspection unchecked
            processFormRepository.save(command.getProcessForm());
        }
    }

    private void initCommandExt(AbstractHandleProcessInstanceCommand command,
                                ProcessInstance instance,
                                ProcessForm form) {
        if (command.getExt() == null) {
            command.setExt(new HashMap<>(2));
        }

        ProcessAction action = null;
        if (instance != null) {
            ProcessDefinition definition = processAppService.processDefinitionOf(
                    instance.getProcessDefinitionId().toString()
            );

            action = definition.availableActionOf(command.getActionId());
        }

        command.getExt().put(ProcessFormContextKeys.PROCESS_FORM, form);

        command.getExt().put(ProcessFormContextKeys.OPERATOR_ACTION_PERMISSIONS,
                operatorPermissionsOf(
                        form,
                        instance,
                        action,
                        command.getOperatorId())
        );
    }

    /**
     * 处理流程表单
     *
     * @param command 流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void start(StartProcessFormCommand command) {
        ProcessForm form = processFormRepository.formOf(command.getProcessForm().getFormId());
        command.getProcessForm().setProcessInstanceId(form.getProcessInstanceId());

        initCommandExt(
                command,
                processAppService.processInstanceOf(form.getProcessInstanceId()),
                command.getProcessForm()
        );
        // TODO check form
        ProcessInstance instance = processAppService.start(
                StartProcessInstanceCommand.Builder
                        .newBuilder()
                        .withActionId(command.getActionId())
                        .withOperatorId(command.getOperatorId())
                        .withProcessInstanceId(form.getProcessInstanceId())
                        .withRemark(command.getRemark())
                        .withExt(command.getExt())
                        .build());
        if (shouldSaveProcessForm(actionOf(instance, command.getActionId()))) {
            //noinspection unchecked
            processFormRepository.save(command.getProcessForm());
        }
    }

    private ProcessFormAction actionOf(ProcessInstance instance,
                                       String actionId) {
        ProcessDefinition definition = processAppService.processDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );

        return (ProcessFormAction) definition.availableActionOf(actionId);
    }

    /**
     * 判断是否需要保存流程表单
     * <p>
     * 默认只有编辑权限的动作才需要保存
     *
     * @param action 当前动作
     */
    protected boolean shouldSaveProcessForm(ProcessFormAction action) {
        return action.hasPermission(ProcessFormAction.Permission.EDIT.name());
    }

    public boolean hasViewPermission(ProcessForm form, ProcessInstance instance, String operatorId) {
        Set<String> permissions = operatorPermissionsOf(form, instance, null, operatorId);
        return permissions.contains(ProcessFormAction.Permission.VIEW.name());
    }

    public Set<String> operatorPermissionsOf(ProcessForm form,
                                             ProcessInstance instance,
                                             ProcessAction action,
                                             String operatorId) {
        return processFormPermissionService.operatorPermissionsOf(
                new ProcessFormPermissionService.Context(
                        form,
                        instance,
                        Optional.ofNullable(instance)
                                .map(it -> processAppService.processNodeChangedEventsOf(it.getProcessInstanceId()))
                                .orElse(Collections.emptyList()),
                        action,
                        operatorId
                )
        );
    }

    /**
     * 当前流程实例可用的动作集合
     */
    public List<ProcessAction> availableActionsOf(ProcessForm form,
                                                  ProcessInstance instance,
                                                  String operatorId) {
        ProcessDefinition definition = processAppService.processDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );
        ProcessNode nextNode = definition.processNodeOf(instance.getCurrentNode().getNextNodeId());
        if (!(nextNode instanceof ActionChoiceNode)) {
            return Collections.emptyList();
        }

        Set<String> permissions = operatorPermissionsOf(form, instance, null, operatorId);

        return ((ActionChoiceNode) nextNode).getActionNodes()
                .stream()
                .map(it -> (ProcessFormAction) definition.availableActionOf(it.getActionId()))
                .filter(it -> it.matchPermissions(permissions))
                .collect(Collectors.toList());
    }
}