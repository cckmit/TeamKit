package org.team4u.kv.infrastruture.repository.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 键值实体类
 *
 * @author jay.wu
 */
@TableName("key_value")
public class KeyValueEntity {

    /**
     * 自增长主键标识
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 过期时间戳
     */
    private Long expirationTimestamp;
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

    public KeyValueEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public KeyValueEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public KeyValueEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public KeyValueEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public KeyValueEntity setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public KeyValueEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public KeyValueEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}