create table `sequence`
(
    `id`                   bigint(20)    not null auto_increment comment '自增长标识',
    `type_id`                 varchar(50)   not null default '' comment '类型',
    `group_key`                 varchar(255)  not null default '' comment '键',
    `current_value`                varchar(4000) not null default '' comment '值',
    `version_number` bigint(20)    not null default '0' comment '过期时间戳',
    `create_time`          timestamp     not null default '1979-01-01 00:00:00' comment '创建时间',
    `update_time`          timestamp     not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `idx_type_name` (`type_id`, `group_key`)
)
;