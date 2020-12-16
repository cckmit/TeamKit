package org.team4u.workflow.domain.instance;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.ddd.domain.model.AggregateRoot;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.event.ProcessInstanceCreatedEvent;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 流程实例
 *
 * @author jay.wu
 */
public class ProcessInstance extends AggregateRoot {

    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程流程定义标识
     */
    private String processDefinitionId;
    /**
     * 当前流程节点
     */
    private StaticNode currentNode;
    /**
     * 当前流程人集合
     */
    private Set<ProcessAssignee> assignees;
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
                           String processInstanceName,
                           String processDefinitionId,
                           StaticNode currentNode,
                           String createdBy) {
        setProcessInstanceId(processInstanceId);
        setProcessInstanceName(processInstanceName);
        setProcessDefinitionId(processDefinitionId);
        setCurrentNode(currentNode);
        setCreateBy(createdBy);

        setAssignees(new HashSet<>());
        setCreateTime(new Date());
    }

    /**
     * 开始新流程
     *
     * @param processInstanceId   流程实例标识
     * @param processInstanceName 流程名称
     * @param processDefinitionId 流程定义标识
     * @param createdBy           创建人
     * @param startNode           开始节点
     * @return 流程实例
     */
    public static ProcessInstance create(String processInstanceId,
                                         String processInstanceName,
                                         String processDefinitionId,
                                         String createdBy,
                                         StaticNode startNode) {
        ProcessInstance instance = new ProcessInstance(
                processInstanceId,
                processInstanceName,
                processDefinitionId,
                startNode,
                createdBy);

        instance.publishEvent(new ProcessInstanceCreatedEvent(
                processDefinitionId,
                instance.getCreateTime(),
                instance
        ));

        return instance;
    }

    /**
     * 更改当前节点
     */
    public void changeCurrentNodeTo(ProcessAction action,
                                    StaticNode newNode,
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

        return getCurrentNode().getNextNodeId() == null;
    }

    /**
     * 查询处理人
     *
     * @param assigneeId 处理人标识
     * @return 流程处理人
     */
    public ProcessAssignee assigneeOf(String assigneeId) {
        return getAssignees().stream()
                .filter(it -> StrUtil.equals(it.getAssignee(), assigneeId))
                .findFirst()
                .orElse(null);
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

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public ProcessInstance setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public ProcessInstance setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }

    public StaticNode getCurrentNode() {
        return currentNode;
    }

    public ProcessInstance setCurrentNode(StaticNode currentNode) {
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

    @Override
    public String toString() {
        return processInstanceId + '/' + processInstanceName;
    }
}