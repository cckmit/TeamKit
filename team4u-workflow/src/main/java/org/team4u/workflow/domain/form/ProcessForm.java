package org.team4u.workflow.domain.form;

import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;

/**
 * 流程表单
 *
 * @author jay.wu
 */
public abstract class ProcessForm extends AggregateRoot {
    /**
     * 流程表单标识
     */
    private String formId;
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程表单内容
     */
    private ProcessFormItem formItem;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public ProcessFormItem getFormItem() {
        return formItem;
    }

    public void setFormItem(ProcessFormItem formItem) {
        this.formItem = formItem;
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