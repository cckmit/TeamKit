package org.team4u.workflow.domain.form;

import org.team4u.ddd.domain.model.AggregateRoot;

import java.util.Date;

/**
 * 表单索引
 *
 * @author jay.wu
 */
public class FormIndex extends AggregateRoot {
    /**
     * 流程实例标识
     */
    private String processInstanceId;
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

    public FormIndex setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public FormIndex setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public FormIndex setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}