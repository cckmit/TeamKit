drop table `process_instance`;
create table `process_instance`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id`        varchar(32)         not null default '' comment '流程实例标识',
    `process_instance_name`      varchar(100)        not null default '' comment '流程实例名称',
    `process_definition_id`      varchar(32)         not null default '' comment '流程流程定义标识',
    `process_definition_version` int unsigned        not null default 0 comment '流程定义版本',
    `current_node_id`            varchar(100)        not null default '' comment '当前流程节点标识',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`),
    index `idx_process_instance_name` (`process_instance_name`),
    index `idx_current_node_id` (`current_node_id`),
    index `idx_create_by` (`create_by`),
    index `idx_update_time` (`update_time`)
) comment ='流程实例';

drop table `process_assignee`;
create table `process_assignee`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(32)         not null default '' comment '流程实例标识',
    `node_id`             varchar(100)        not null default '' comment '流程节点标识',
    `action_id`           varchar(32)         not null default '' comment '动作',
    `assignee`            varchar(32)         not null default '' comment '审批人',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `idx_process_instance_id` (`process_instance_id`, node_id),
    index `idx_assignee` (`assignee`)
) comment ='流程处理人';

drop table `process_definition`;
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
    unique index `uniq_process_definition_id` (`process_definition_id`, process_definition_version),
    index `idx_update_time` (`update_time`)
) comment ='流程定义';

drop table `test_form`;
create table if not exists `test_form`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `form_id`             varchar(32)         not null default '' comment '表单标识',
    `process_instance_id` varchar(32)         not null default '' comment '流程实例标识',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp,
    primary key (`id`),
    unique index `uniq_form_id` (`form_id`)
) comment ='测试表单';

drop table `process_form_item`;
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
) comment ='流程表单明细';