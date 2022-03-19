package org.team4u.config.infrastructure.persistence.jdbc;

import lombok.Data;

import java.util.Date;

@Data
public class SystemConfigDo {

    private Long id;

    private String configType;

    private String configKey;

    private String configValue;

    private String description;

    private int sequenceNo;

    private Boolean enabled;

    private String createdBy;

    private String updatedBy;

    private Date createTime;

    private Date updateTime;
}
