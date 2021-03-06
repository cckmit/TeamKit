package org.team4u.workflow.infrastructure.persistence.instance;

import com.baomidou.mybatisplus.annotation.*;
import org.team4u.workflow.infrastructure.persistence.instance.ext.MybatisDetailTypeHandler;

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
     * 流程实例类型
     */
    private String processInstanceType;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程实例明细
     */
    @TableField(typeHandler = MybatisDetailTypeHandler.class)
    private String processInstanceDetail;
    /**
     * 流程流程定义标识
     */
    private String processDefinitionId;
    /**
     * 流程流程定义版本
     */
    private int processDefinitionVersion;
    /**
     * 当前流程节点标识
     */
    private String currentNodeId;
    /**
     * 当前流程节点名称
     */
    private String currentNodeName;
    /**
     * 乐观锁
     */
    @Version
    private int concurrencyVersion = 0;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceType() {
        return processInstanceType;
    }

    public void setProcessInstanceType(String processInstanceType) {
        this.processInstanceType = processInstanceType;
    }

    public String getProcessInstanceName() {
        return processInstanceName;
    }

    public void setProcessInstanceName(String processInstanceName) {
        this.processInstanceName = processInstanceName;
    }

    public String getProcessInstanceDetail() {
        return processInstanceDetail;
    }

    public void setProcessInstanceDetail(String processInstanceDetail) {
        this.processInstanceDetail = processInstanceDetail;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public int getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(int processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getCurrentNodeName() {
        return currentNodeName;
    }

    public void setCurrentNodeName(String currentNodeName) {
        this.currentNodeName = currentNodeName;
    }

    public int getConcurrencyVersion() {
        return concurrencyVersion;
    }

    public void setConcurrencyVersion(int concurrencyVersion) {
        this.concurrencyVersion = concurrencyVersion;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}