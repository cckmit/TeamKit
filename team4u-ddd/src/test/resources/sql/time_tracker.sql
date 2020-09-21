CREATE TABLE `time_constrained_process_tracker`
(
    `id`                           BIGINT(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长标识',
    `tracker_id`                   VARCHAR(32)         NOT NULL DEFAULT '' COMMENT '跟踪器标识',
    `process_id`                   VARCHAR(32)         NOT NULL DEFAULT '' COMMENT '关联处理标识',
    `completed`                    TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '是否完成(0:未完成，1:已完成)',
    `description`                  VARCHAR(512)        NOT NULL DEFAULT '' COMMENT '描述',
    `process_informed_of_timeout`  TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '是否完成所有超时通知',
    `process_timed_out_event_type` VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '超时时间类型',
    `timeout_occurs_on`            TIMESTAMP           NOT NULL DEFAULT '1970-01-01 23:59:59' COMMENT '超时时间',
    `retry_strategy_id`            VARCHAR(100)        NOT NULL DEFAULT '60' COMMENT '重试策略标识',
    `retry_count`                  INT(11)             NOT NULL DEFAULT '0' COMMENT '已重试次数',
    `version`                      BIGINT(20) unsigned NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    `create_time`                  TIMESTAMP           NOT NULL DEFAULT '1970-01-01 23:59:59' COMMENT '创建时间',
    `update_time`                  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uniq_tracker_id` (`tracker_id`),
    UNIQUE INDEX `uniq_tracker_process_id_type` (`process_id`, `process_timed_out_event_type`)
)
    COMMENT ='超时跟踪器'
;
