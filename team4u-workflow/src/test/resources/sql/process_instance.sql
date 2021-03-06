create table `process_instance`
(
    `id`                         bigint(20) not null auto_increment comment '自增长标识',
    `process_instance_id`        varchar(32)         not null default '' comment '流程实例标识',
    `process_instance_type`      varchar(32)         not null default '' comment '流程实例类型',
    `process_instance_name`      varchar(100)        not null default '' comment '流程实例名称',
    `process_instance_detail`    varchar(1000)       not null default '' comment '流程明细内容',
    `process_definition_id`      varchar(32)         not null default '' comment '流程流程定义标识',
    `process_definition_version` int        not null default 0 comment '流程定义版本',
    `current_node_id`            varchar(32)         not null default '' comment '当前流程节点标识',
    `current_node_name`          varchar(100)        not null default '' comment '当前流程节点名称',
    `concurrency_version`        int        not null default 0 comment '乐观锁版本',
    `create_by`                  varchar(32)         not null default '' comment '创建者标识',
    `update_by`                  varchar(32)         not null default '' comment '编辑者标识',
    `create_time`                timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`                timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_process_instance_id` (`process_instance_id`)
);

create table `process_assignee`
(
    `id`                  bigint(20) not null auto_increment comment '自增长标识',
    `process_instance_id` varchar(32)         not null default '' comment '流程实例标识',
    `node_id`             varchar(100)        not null default '' comment '流程节点标识',
    `action_id`           varchar(32)         not null default '' comment '动作',
    `assignee`            varchar(32)         not null default '' comment '审批人',
    `create_time`         timestamp           not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time`         timestamp           not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `idx_process_assignee` (`process_instance_id`, node_id)
);