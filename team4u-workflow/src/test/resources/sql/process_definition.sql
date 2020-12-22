create table `process_definition`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_definition_id`      varchar(32)         not null default '' comment '流程定义版本',
    `process_definition_version` BIGINT(20) unsigned not null default 0 comment '流程定义版本',
    `process_definition_body`    varchar(21800)      not null default '' comment '流程定义内容',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_definition_id` (`process_definition_id`, process_definition_version)
) comment ='流程定义';
