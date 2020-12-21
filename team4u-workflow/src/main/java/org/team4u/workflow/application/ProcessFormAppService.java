package org.team4u.workflow.application;

import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
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
import org.team4u.workflow.domain.form.exception.ProcessFormNotExistException;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.instance.NoPermissionException;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程表单抽象应用服务
 *
 * @author jay.wu
 */
public class ProcessFormAppService {

    private final EventStore eventStore;

    private final ProcessAppService processAppService;
    @SuppressWarnings("rawtypes")
    private final ProcessFormRepository processFormRepository;
    private final ProcessFormPermissionService processFormPermissionService;

    public ProcessFormAppService(EventStore eventStore,
                                 ProcessAppService processAppService,
                                 ProcessFormRepository<?> processFormRepository,
                                 ProcessFormPermissionService processFormPermissionService) {
        this.eventStore = eventStore;
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
        if (formModel.getForm() == null) {
            throw new ProcessFormNotExistException(formId);
        }

        formModel.setInstance(processAppService.availableProcessInstanceOf(
                formModel.getForm().getProcessInstanceId()
        ));

        if (!hasViewPermission(formModel.getForm(), formModel.getInstance(), operatorId)) {
            throw new NoPermissionException("您没有权限查看请表单");
        }

        formModel.setActions(availableActionsOf(formModel.getForm(), formModel.getInstance(), operatorId));

        formModel.setEvents(processNodeChangedEventsOf(
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
        // 准备数据
        ProcessFormAction action = actionOf(command.getProcessDefinitionId(), command.getActionId());
        initCommandExt(command, command.getProcessForm(), null, action);

        // 开始流程处理
        ProcessInstance instance = processAppService.create(command);

        // 保存表单
        command.getProcessForm().setProcessInstanceId(instance.getProcessInstanceId());
        saveForm(action, command.getProcessForm());
    }

    /**
     * 处理流程表单
     *
     * @param command 流程表单命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void start(StartProcessFormCommand command) {
        // 准备数据
        ProcessForm newForm = command.getProcessForm();
        ProcessForm oldForm = processFormRepository.formOf(newForm.getFormId());

        if (oldForm == null) {
            throw new ProcessFormNotExistException(newForm.getFormId());
        }

        ProcessInstance instance = processAppService.availableProcessInstanceOf(oldForm.getProcessInstanceId());
        ProcessFormAction action = actionOf(instance.getProcessDefinitionId().toString(), command.getActionId());

        newForm.setProcessInstanceId(instance.getProcessInstanceId());
        initCommandExt(command, newForm, instance, action);

        // 开始流程处理
        processAppService.start(
                StartProcessInstanceCommand.Builder
                        .newBuilder()
                        .withActionId(command.getActionId())
                        .withOperatorId(command.getOperatorId())
                        .withProcessInstanceId(oldForm.getProcessInstanceId())
                        .withRemark(command.getRemark())
                        .withExt(command.getExt())
                        .build()
        );

        // 保存表单
        saveForm(action, newForm);
    }

    /**
     * 当前流程实例可用的动作集合
     */
    public List<ProcessAction> availableActionsOf(ProcessForm form,
                                                  ProcessInstance instance,
                                                  String operatorId) {
        ProcessDefinition definition = processAppService.availableProcessDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );
        ProcessNode nextNode = definition.availableProcessNodeOf(instance.getCurrentNode().getNextNodeId());
        // 仅处理ActionChoiceNode
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

    /**
     * 获取节点变动日志
     */
    public List<ProcessNodeChangedEvent> processNodeChangedEventsOf(String processInstanceId) {
        if (StrUtil.isBlank(processInstanceId)) {
            return Collections.emptyList();
        }

        return eventStore.allStoredEventsOf(processInstanceId)
                .stream()
                .filter(it -> StrUtil.equals(it.typeName(), ProcessNodeChangedEvent.class.getName()))
                .map(StoredEvent::<ProcessNodeChangedEvent>toDomainEvent)
                .collect(Collectors.toList());
    }

    private ProcessFormAction actionOf(String processDefinitionId,
                                       String actionId) {
        ProcessDefinition definition = processAppService.availableProcessDefinitionOf(
                processDefinitionId
        );

        return (ProcessFormAction) definition.availableActionOf(actionId);
    }

    private boolean hasViewPermission(ProcessForm form, ProcessInstance instance, String operatorId) {
        Set<String> permissions = operatorPermissionsOf(form, instance, null, operatorId);
        return permissions.contains(ProcessFormAction.Permission.VIEW.name());
    }

    private Set<String> operatorPermissionsOf(ProcessForm form,
                                              ProcessInstance instance,
                                              ProcessAction action,
                                              String operatorId) {
        return processFormPermissionService.operatorPermissionsOf(
                new ProcessFormPermissionService.Context(
                        form,
                        instance,
                        Optional.ofNullable(instance)
                                .map(it -> processNodeChangedEventsOf(it.getProcessInstanceId()))
                                .orElse(Collections.emptyList()),
                        action,
                        operatorId
                )
        );
    }

    private void saveForm(ProcessFormAction action, ProcessForm form) {
        if (processFormPermissionService.shouldSaveProcessForm(action)) {
            //noinspection unchecked
            processFormRepository.save(form);
        }
    }

    private void initCommandExt(AbstractHandleProcessInstanceCommand command,
                                ProcessForm form,
                                ProcessInstance instance,
                                ProcessFormAction action) {
        if (command.getExt() == null) {
            command.setExt(new HashMap<>(2));
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
}