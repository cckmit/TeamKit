package org.team4u.workflow.domain.instance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.ConcurrencySafeAggregateRoot;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.event.ProcessInstanceCreatedEvent;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程实例
 *
 * @author jay.wu
 */
public class ProcessInstance extends ConcurrencySafeAggregateRoot {

    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程实例类型
     */
    private String processInstanceType;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程流程定义标识
     */
    private ProcessDefinitionId processDefinitionId;
    /**
     * 当前流程节点
     */
    private ProcessNode currentNode;
    /**
     * 当前流程处理人集合
     */
    private Set<ProcessAssignee> assignees;
    /**
     * 流程实例明细
     */
    private ProcessInstanceDetail processInstanceDetail;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public ProcessInstance(String processInstanceId,
                           String processInstanceType,
                           String processInstanceName,
                           ProcessDefinitionId processDefinitionId,
                           ProcessNode currentNode,
                           String createdBy,
                           Object processInstanceDetail) {
        setProcessInstanceId(processInstanceId);
        setProcessInstanceType(processInstanceType);
        setProcessInstanceName(processInstanceName);
        setProcessDefinitionId(processDefinitionId);
        setCurrentNode(currentNode);
        setCreateBy(createdBy);
        setProcessInstanceDetail(processInstanceDetail);

        setAssignees(new HashSet<>());
        setCreateTime(new Date());
    }

    /**
     * 开始新流程
     *
     * @param processInstanceId   流程实例标识
     * @param processInstanceType 流程实例类型
     * @param processInstanceName 流程名称
     * @param processDefinitionId 流程定义标识
     * @param createdBy           创建人
     * @param startNode           开始节点
     * @return 流程实例
     */
    public static ProcessInstance create(String processInstanceId,
                                         String processInstanceType,
                                         String processInstanceName,
                                         ProcessDefinitionId processDefinitionId,
                                         String createdBy,
                                         ProcessNode startNode,
                                         Object processInstanceDetail) {
        ProcessInstance instance = new ProcessInstance(
                processInstanceId,
                processInstanceType,
                processInstanceName,
                processDefinitionId,
                startNode,
                createdBy,
                processInstanceDetail
        );

        instance.publishEvent(new ProcessInstanceCreatedEvent(
                instance.getProcessInstanceId(),
                instance.getProcessInstanceName(),
                instance.getCreateTime(),
                instance.getCreateBy(),
                instance.getCurrentNode().getNodeId(),
                instance.getProcessDefinitionId()
        ));

        return instance;
    }

    /**
     * 更改当前节点
     */
    public void changeCurrentNodeTo(ProcessAction action,
                                    ProcessNode newNode,
                                    String operator,
                                    String remark) {
        ProcessNode oldNode = ObjectUtil.defaultIfNull(getCurrentNode(), newNode);
        setCurrentNode(newNode);

        refreshUpdateTimeAndOperator(operator);

        publishEvent(new ProcessNodeChangedEvent(
                getProcessInstanceId(),
                getUpdateTime(),
                oldNode.getNodeId(),
                oldNode.getNodeName(),
                action.getActionId(),
                action.getActionName(),
                newNode.getNodeId(),
                newNode.getNodeName(),
                remark,
                operator
        ));
    }

    /**
     * 流程是否已完成
     */
    public boolean isCompleted() {
        if (getCurrentNode() == null) {
            return false;
        }

        if (getCurrentNode() instanceof StaticNode) {
            return ((StaticNode) getCurrentNode()).getNextNodeId() == null;
        }

        return false;
    }

    /**
     * 查询当前处理人
     *
     * @param assigneeId 处理人标识
     * @return 流程处理人
     */
    public ProcessAssignee currentAssigneeOf(String assigneeId) {
        if (getCurrentNode() == null) {
            return null;
        }

        return getAssignees().stream()
                .filter(it -> StrUtil.equals(it.getAssignee(), assigneeId))
                .filter(it -> StrUtil.equals(it.getNodeId(), getCurrentNode().getNodeId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 当前处理人集合
     */
    public List<ProcessAssignee> currentAssignees() {
        return getAssignees().stream()
                .filter(it -> StrUtil.equals(it.getNodeId(), getCurrentNode().getNodeId()))
                .collect(Collectors.toList());
    }

    private void refreshUpdateTimeAndOperator(String operator) {
        setUpdateTime(new Date());
        setUpdateBy(operator);
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessInstance setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getProcessInstanceType() {
        return processInstanceType;
    }

    public ProcessInstance setProcessInstanceType(String processInstanceType) {
        this.processInstanceType = processInstanceType;
        return this;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public ProcessInstance setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public ProcessDefinitionId getProcessDefinitionId() {
        return processDefinitionId;
    }

    public ProcessInstance setProcessDefinitionId(ProcessDefinitionId processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <N extends ProcessNode> N getCurrentNode() {
        return (N) currentNode;
    }

    public ProcessInstance setCurrentNode(ProcessNode currentNode) {
        this.currentNode = currentNode;
        return this;
    }

    public Set<ProcessAssignee> getAssignees() {
        return assignees;
    }

    public ProcessInstance setAssignees(Set<ProcessAssignee> assignees) {
        this.assignees = assignees;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public ProcessInstance setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public ProcessInstance setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessInstance setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ProcessInstance setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public ProcessInstanceDetail getProcessInstanceDetail() {
        return processInstanceDetail;
    }

    public ProcessInstance setProcessInstanceDetail(Object processInstanceDetail) {
        this.processInstanceDetail = new ProcessInstanceDetail(processInstanceDetail);
        return this;
    }

    @Override
    public String toString() {
        if (StrUtil.isBlank(processInstanceName)) {
            return processInstanceId;
        }

        return processInstanceId + '/' + processInstanceName;
    }
}