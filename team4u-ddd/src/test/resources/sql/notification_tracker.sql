CREATE TABLE `published_notification_tracker`
(
    `id`                                    BIGINT(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长标识',
    `tracker_id`                            VARCHAR(32)         NOT NULL DEFAULT '' COMMENT '跟踪器标识',
    `most_recent_published_notification_id` BIGINT(20)          NOT NULL DEFAULT 0 COMMENT '最近发布的通知标识',
    `type_name`                             VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '通知类型名称',
    `version`                               BIGINT(20) unsigned NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    `create_time`                           TIMESTAMP           NOT NULL DEFAULT '1970-01-01 23:59:59' COMMENT '创建时间',
    `update_time`                           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uniq_tracker_id` (`tracker_id`),
    UNIQUE INDEX `uniq_type_name` (`type_name`)
)