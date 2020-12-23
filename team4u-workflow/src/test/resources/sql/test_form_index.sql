create table if not exists test_form_index
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(100)        not null default '' comment '表单名称',
    `name`                varchar(32)         not null default '' comment '流程实例标识',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `uniq_process_instance_id1` (`process_instance_id`)
) comment ='测试表单索引';
