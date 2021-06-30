drop table if exists `process_instance`;
create table `process_instance`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id`        varchar(32)         not null default '' comment '流程实例标识',
    `process_instance_type`      varchar(32)         not null default '' comment '流程实例类型',
    `process_instance_name`      varchar(100)        not null default '' comment '流程实例名称',
    `process_instance_body`      JSON                not null comment '流程明细内容',
    `process_definition_id`      varchar(32)         not null default '' comment '流程流程定义标识',
    `process_definition_version` int unsigned        not null default 0 comment '流程定义版本',
    `process_definition_name`    varchar(100)        not null default '' comment '流程流程定义名称',
    `current_node_id`            varchar(32)         not null default '' comment '当前流程节点标识',
    `current_node_name`          varchar(100)        not null default '' comment '当前流程节点名称',
    `concurrency_version`        int unsigned        not null default 0 comment '乐观锁版本',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`),
    index `idx_process_instance_type` (`process_instance_type`),
    index `idx_process_instance_name` (`process_instance_name`),
    index `idx_current_node_id` (`current_node_id`),
    index `idx_process_definition_id` (`process_definition_id`),
    index `idx_create_by` (`create_by`),
    index `idx_update_time` (`update_time`)
) comment ='流程实例';

drop table if exists `process_assignee`;
create table `process_assignee`
(
    `id`                  bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(50)         not null default '' comment '流程实例标识',
    `node_id`             varchar(100)        not null default '' comment '流程节点标识',
    `action_id`           varchar(32)         not null default '' comment '动作',
    `assignee`            varchar(32)         not null default '' comment '审批人',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_assignee` (`process_instance_id`, node_id),
    index `idx_assignee` (`assignee`)
) comment ='流程处理人';

drop table if exists `process_definition`;
create table `process_definition`
(
    `id`                         bigint(20) unsigned not null auto_increment comment '自增长标识',
    `process_definition_id`      varchar(32)         not null default '' comment '流程定义版本',
    `process_definition_version` int unsigned        not null default 0 comment '流程定义版本',
    `process_definition_name`    varchar(100)        not null default '' comment '流程定义名称',
    `process_definition_body`    varchar(21000)      not null default '' comment '流程定义内容',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_definition_id` (`process_definition_id`, process_definition_version),
    index `idx_process_definition_name` (`process_definition_name`),
    index `idx_update_time` (`update_time`)
) comment ='流程定义';

drop table if exists `stored_event`;
create table `stored_event`
(
    `id`          bigint(20) unsigned not null auto_increment comment '自增长标识',
    `event_id`    bigint(20) unsigned not null default 0 comment '事件标识',
    `domain_id`   varchar(32)         not null default '' comment '领域标识',
    `type_name`   varchar(255)        not null default '' comment '事件类型名称',
    `event_body`  JSON                not null comment '事件值',
    `occurred_on` timestamp           not null default '1970-01-01 23:59:59' comment '发生时间',
    `create_time` timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time` timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_event_id` (event_id),
    index `idx_domain_id` (domain_id)
)
    comment ='存储的事件'
;