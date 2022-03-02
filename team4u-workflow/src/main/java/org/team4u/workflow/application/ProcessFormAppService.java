package org.team4u.workflow.application;

import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.team4u.workflow.application.command.CreateProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessFormCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.application.model.ProcessFormModel;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.form.FormContextKeys;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.form.FormIndexRepository;
import org.team4u.workflow.domain.form.FormPermissionService;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.form.process.node.handler.AssigneeActionChoiceNodeHandler;
import org.team4u.workflow.domain.form.process.node.handler.AssigneeStaticNodeHandler;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;
import org.team4u.workflow.domain.instance.exception.NoPermissionException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 表单索引抽象应用服务
 *
 * @author jay.wu
 */
public class ProcessFormAppService {

    private final EventStore eventStore;

    private final ProcessAppService processAppService;
    @SuppressWarnings("rawtypes")
    private final FormIndexRepository formIndexRepository;
    private final FormPermissionService formPermissionService;

    public ProcessFormAppService(EventStore eventStore,
                                 ProcessAppService processAppService,
                                 FormPermissionService formPermissionService) {
        this(eventStore, processAppService, null, formPermissionService);
    }

    public ProcessFormAppService(EventStore eventStore,
                                 ProcessAppService processAppService,
                                 FormIndexRepository<?> formIndexRepository,
                                 FormPermissionService formPermissionService) {
        this.eventStore = eventStore;
        this.processAppService = processAppService;
        this.formIndexRepository = formIndexRepository;
        this.formPermissionService = formPermissionService;
    }

    /**
     * 获取表单索引模型
     *
     * @param instanceId 流程实例标识
     * @param operatorId 当前处理人
     * @return 表单索引模型
     */
    public ProcessFormModel formOf(String instanceId, String operatorId) {
        ProcessFormModel formModel = new ProcessFormModel();

        if (formIndexRepository != null) {
            formModel.setFormIndex(formIndexRepository.formOf(instanceId));
        }

        formModel.setInstance(processAppService.availableProcessInstanceOf(instanceId));

        ProcessDefinition definition = processAppService.availableProcessDefinitionOf(
                formModel.getInstance().getProcessDefinitionId().toString()
        );

        if (!hasViewPermission(formModel.getFormIndex(), formModel.getInstance(), definition, operatorId)) {
            throw new NoPermissionException("您没有权限查看请表单");
        }

        formModel.setPermissions(operatorPermissionsOf(
                formModel.getFormIndex(),
                formModel.getInstance(),
                definition,
                null,
                operatorId
        ));

        formModel.setActions(availableActionsOf(
                formModel.getFormIndex(),
                formModel.getInstance(),
                formModel.getPermissions(),
                definition,
                operatorId
        ));

        formModel.setEvents(processNodeChangedEventsOf(
                formModel.getInstance().getProcessInstanceId()
        ));
        return formModel;
    }

    /**
     * 创建表单索引
     *
     * @param command 表单索引命令
     * @return 流程实例标识
     */
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateProcessFormCommand command) {
        // 创建流程
        ProcessInstance instance = processAppService.create(command);
        // 创建表单
        saveForm(instance, null, command.getFormIndex());

        return instance.getProcessInstanceId();
    }

    /**
     * 处理表单索引
     *
     * @param command 表单索引命令
     */
    @Transactional(rollbackFor = Exception.class)
    public void start(StartProcessFormCommand command) {
        // 准备数据
        ProcessInstance instance = processAppService.availableProcessInstanceOf(command.getProcessInstanceId());

        FormIndex newForm = command.getFormIndex();
        if (newForm != null) {
            newForm.setProcessInstanceId(instance.getProcessInstanceId());

            FormIndex oldForm = formIndexRepository.formOf(instance.getProcessInstanceId());
            newForm.setId(oldForm.getId());
        }

        ProcessDefinition definition = processAppService.availableProcessDefinitionOf(
                instance.getProcessDefinitionId().toString()
        );
        ProcessFormAction action = actionOf(definition, command.getActionId());
        initCommandExt(command, newForm, instance, definition, action);

        // 保存表单
        boolean isSaveForm = saveForm(instance, action, newForm);

        // 处理流程
        processAppService.start(
                StartProcessInstanceCommand.builder()
                        .actionId(command.getActionId())
                        .operatorId(command.getOperatorId())
                        .processInstanceId(command.getProcessInstanceId())
                        // 不保存表单，流程实例明细也不做保存
                        .processInstanceDetail(isSaveForm ? command.getProcessInstanceDetail() : null)
                        .remark(command.getRemark())
                        .ext(command.getExt())
                        .build()
        );
    }

    /**
     * 当前流程实例可用的动作集合
     */
    public List<ProcessAction> availableActionsOf(FormIndex form,
                                                  ProcessInstance instance,
                                                  Set<String> permissions,
                                                  ProcessDefinition definition,
                                                  String operatorId) {
        if (!(instance.getCurrentNode() instanceof StaticNode)) {
            return Collections.emptyList();
        }

        StaticNode currentNode = instance.getCurrentNode();
        ProcessNode nextNode = definition.processNodeOf(currentNode.getNextNodeId());

        // 仅处理ActionChoiceNode
        if (!(nextNode instanceof ActionChoiceNode)) {
            return Collections.emptyList();
        }

        return ((ActionChoiceNode) nextNode).getActionNodes()
                .stream()
                .map(it -> actionOf(definition, it.getActionId()))
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

    /**
     * 获取工作流应用服务
     *
     * @return 工作流应用服务
     */
    public ProcessAppService getProcessAppService() {
        return processAppService;
    }

    /**
     * 注册处理人策略
     *
     * @param policy 策略
     */
    public void registerAssigneePolicy(AssigneeStaticNodeHandler.Policy policy) {
        AssigneeStaticNodeHandler handler = processAppService.processNodeHandlerOf(AssigneeStaticNode.class);
        handler.registerPolicy(policy);
    }

    /**
     * 注册处理人动作策略
     *
     * @param policy 策略
     */
    public void registerAssigneeActionPolicy(AssigneeActionChoiceNodeHandler.Policy policy) {
        AssigneeActionChoiceNodeHandler handler = processAppService.processNodeHandlerOf(AssigneeActionChoiceNode.class);
        handler.registerPolicy(policy);
    }

    private ProcessFormAction actionOf(ProcessDefinition definition, String actionId) {
        ProcessAction action = definition.availableActionOf(actionId);
        if (action instanceof ProcessFormAction) {
            return (ProcessFormAction) action;
        }

        return new ProcessFormAction(actionId, action.getActionName(), Collections.emptyList());
    }

    private boolean hasViewPermission(FormIndex form,
                                      ProcessInstance instance,
                                      ProcessDefinition definition,
                                      String operatorId) {
        Set<String> permissions = operatorPermissionsOf(form, instance, definition, null, operatorId);
        return permissions.contains(ProcessFormAction.Permission.VIEW.name());
    }

    public Set<String> operatorPermissionsOf(FormIndex form,
                                             ProcessInstance instance,
                                             ProcessDefinition definition,
                                             ProcessAction action,
                                             String operatorId) {
        return formPermissionService.operatorPermissionsOf(
                new FormPermissionService.Context(
                        form,
                        instance,
                        definition,
                        Optional.ofNullable(instance)
                                .map(it -> processNodeChangedEventsOf(it.getProcessInstanceId()))
                                .orElse(Collections.emptyList()),
                        action,
                        operatorId
                )
        );
    }

    private boolean saveForm(ProcessInstance instance, ProcessFormAction action, FormIndex form) {
        if (formPermissionService.shouldSaveProcessForm(action)) {
            if (form == null || formIndexRepository == null) {
                return true;
            }

            form.setProcessInstanceId(instance.getProcessInstanceId());
            //noinspection unchecked
            formIndexRepository.save(form);
            return true;
        }

        return false;
    }

    private void initCommandExt(StartProcessFormCommand command,
                                FormIndex formIndex,
                                ProcessInstance instance,
                                ProcessDefinition definition,
                                ProcessFormAction action) {
        if (command.getExt() == null) {
            command.setExt(new HashMap<>(2));
        }

        command.getExt().put(FormContextKeys.PROCESS_FORM, formIndex);

        command.getExt().put(FormContextKeys.OPERATOR_ACTION_PERMISSIONS,
                operatorPermissionsOf(
                        formIndex,
                        instance,
                        definition,
                        action,
                        command.getOperatorId())
        );
    }
}