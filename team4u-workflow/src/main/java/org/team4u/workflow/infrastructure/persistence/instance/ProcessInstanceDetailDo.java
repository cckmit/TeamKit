package org.team4u.workflow.infrastructure.persistence.instance;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 流程实例明细数据
 *
 * @author jay.wu
 */
@TableName("process_instance_detail")
public class ProcessInstanceDetailDo {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 流程实例标识
     */
    private String processInstanceId;
    /**
     * 流程实例明细类型
     */
    private String type;
    /**
     * 流程实例明细内容
     */
    private String body;
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

    public ProcessInstanceDetailDo setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public ProcessInstanceDetailDo setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }

    public String getType() {
        return type;
    }

    public ProcessInstanceDetailDo setType(String type) {
        this.type = type;
        return this;
    }

    public String getBody() {
        return body;
    }

    public ProcessInstanceDetailDo setBody(String body) {
        this.body = body;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ProcessInstanceDetailDo setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ProcessInstanceDetailDo setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}