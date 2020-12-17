package org.team4u.workflow.infrastructure.persistence.instance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 流程实例数据对象
 *
 * @author jay.wu
 */
@TableName("process_instance")
public class ProcessInstanceDo {
    @TableId(type = IdType.AUTO)
    private Long id;
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
     * 流程流程定义版本
     */
    private long processDefinitionVersion;
    /**
     * 当前流程节点标识
     */
    private String currentNodeId;
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

    public Long getId() {
        return id;
    }

    public ProcessInstanceDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessInstanceDo setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public ProcessInstanceDo setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
        return this;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public ProcessInstanceDo setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }

    public long getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public ProcessInstanceDo setProcessDefinitionVersion(long processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
        return this;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public ProcessInstanceDo setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public ProcessInstanceDo setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public ProcessInstanceDo setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessInstanceDo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ProcessInstanceDo setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}