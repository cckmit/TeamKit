CREATE TABLE `system_config`
(
    `id`           BIGINT(20) UNSIGNED     NOT NULL AUTO_INCREMENT,
    `enabled`      TINYINT      default 1  not null comment '是否开启',
    `config_type`  varchar(32)  default '' not null comment '配置类型',
    `config_key`   varchar(50)  default '' not null comment '配置键',
    `config_value` varchar(500) default '' not null comment '配置值',
    `sequence_no`  bigint       default 0  not null comment '序号',
    `description`  varchar(255) default '' not null comment '描述',
    `created_by`   varchar(50)  default '' not null comment '创建者',
    `updated_by`   varchar(50)  default '' not null comment '更新者',
    `create_time`  TIMESTAMP               NOT NULL DEFAULT '1970-01-01 23:59:59' COMMENT '创建时间',
    `update_time`  TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX uniq_config_id (`config_type`, `config_key`)
);