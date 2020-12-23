package org.team4u.workflow.domain.form;

import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;

/**
 * 流程表单
 *
 * @author jay.wu
 */
public class ProcessForm extends AggregateRoot {
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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessForm setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public ProcessFormItem getFormItem() {
        return formItem;
    }

    public ProcessForm setFormItem(ProcessFormItem formItem) {
        this.formItem = formItem;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessForm setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ProcessForm setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}