package org.team4u.ddd.infrastructure.persistence.mybatis;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("idempotent_value")
public class IdempotentValueEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String idempotentId;

    private String typeName;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public IdempotentValueEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getIdempotentId() {
        return idempotentId;
    }

    public IdempotentValueEntity setIdempotentId(String idempotentId) {
        this.idempotentId = idempotentId;
        return this;
    }

    public String getTypeName() {
        return typeName;
    }

    public IdempotentValueEntity setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public IdempotentValueEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public IdempotentValueEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}