create table `sequence`
(
    `id`             bigint(20)   not null auto_increment comment '自增长标识',
    `config_id`      varchar(50)  not null default '' comment '配置标识',
    `group_key`      varchar(100) not null default '' comment '分组标识',
    `current_value`  bigint(20)   not null default '' comment '当前序号',
    `version_number` bigint(20)   not null default '0' comment '版本号',
    `create_time`    timestamp    not null default '1979-01-01 00:00:00' comment '创建时间',
    `update_time`    timestamp    not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `idx_type_group` (`config_id`, `group_key`)
)
;