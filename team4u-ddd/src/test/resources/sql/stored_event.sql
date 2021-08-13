create table `stored_event`
(
    `id`          bigint(20)    not null auto_increment comment '自增长标识',
    `event_id`    bigint(20)    not null default 0 comment '事件标识',
    `domain_id`   varchar(32)   not null default '' comment '领域标识',
    `type_name`   varchar(255)  not null default '' comment '事件类型名称',
    `event_body`  varchar(4000) not null default '' comment '事件值',
    `occurred_on` timestamp     not null default '1970-01-01 23:59:59' comment '发生时间',
    `create_time` timestamp     not null default '1970-01-01 23:59:59' comment '创建时间',
    `update_time` timestamp     not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`),
    unique index `uniq_event_id` (event_id)
);