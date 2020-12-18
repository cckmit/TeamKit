create table if not exists `test_form`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `form_id`             varchar(32)         not null default '' comment '表单标识',
    `process_instance_id` varchar(100)        not null default '' comment '表单名称',
    `name`                varchar(32)         not null default '' comment '流程实例标识',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `uniq_form_id` (`form_id`)
) comment ='测试表单';

create table if not exists `process_form_item`
(
    `id`             bigint(20) unsigned not null auto_increment comment '自增长标识',
    `form_id`        varchar(32)         not null default '' comment '表单标识',
    `form_body_type` varchar(100)        not null default '' comment '表单类型',
    `form_body`      varchar(21800)      not null default '' comment '表单明细',
    `create_time`    timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`    timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `idx_form_id` (`form_id`)
) comment ='表单明细';