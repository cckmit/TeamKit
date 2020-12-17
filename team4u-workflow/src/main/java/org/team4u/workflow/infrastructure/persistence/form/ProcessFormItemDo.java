package org.team4u.workflow.infrastructure.persistence.form;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 流程表单内容数据
 *
 * @author jay.wu
 */
@TableName("form_item")
public class ProcessFormItemDo {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 流程表单标识
     */
    private String formId;
    /**
     * 表单内容类型
     */
    private String formBodyType;
    /**
     * 流程表单内容
     */
    private String formBody;
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

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormBodyType() {
        return formBodyType;
    }

    public void setFormBodyType(String formBodyType) {
        this.formBodyType = formBodyType;
    }

    public String getFormBody() {
        return formBody;
    }

    public void setFormBody(String formBody) {
        this.formBody = formBody;
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