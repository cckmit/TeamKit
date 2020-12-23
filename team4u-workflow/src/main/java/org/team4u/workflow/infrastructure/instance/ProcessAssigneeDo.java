package org.team4u.workflow.infrastructure.instance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 流程处理人数据对象
 *
 * @author jay.wu
 */
@TableName("process_assignee")
public class ProcessAssigneeDo {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 节点标识
     */
    private String nodeId;
    /**
     * 处理人
     */
    private String assignee;
    /**
     * 流程动作
     */
    private String actionId;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public ProcessAssigneeDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessAssigneeDo setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getNodeId() {
        return nodeId;
    }

    public ProcessAssigneeDo setNodeId(String nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getAssignee() {
        return assignee;
    }

    public ProcessAssigneeDo setAssignee(String assignee) {
        this.assignee = assignee;
        return this;
    }

    public String getActionId() {
        return actionId;
    }

    public ProcessAssigneeDo setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessAssigneeDo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ProcessAssigneeDo setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}