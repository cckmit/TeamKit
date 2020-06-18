CREATE TABLE idempotent_value
(
    id            BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '自增长标识',
    idempotent_id VARCHAR(50) NOT NULL DEFAULT '' COMMENT '幂等标识',
    type_name     VARCHAR(32) NOT NULL DEFAULT '' COMMENT '通知类型标识',
    create_time   TIMESTAMP   NOT NULL DEFAULT '1970-01-01 23:59:59' COMMENT '创建时间',
    update_time   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE INDEX `uniq_idempotent_id` (`type_name`,`idempotent_id`),
);