package org.team4u.base.id;


import cn.hutool.core.lang.Snowflake;

/**
 * 基于雪花算法的主键生成器
 *
 * @author jay.wu
 */
public class SnowflakeIdentityFactory implements NumberIdentityFactory {

    /**
     * 终端ID
     */
    private final long workerId;
    /**
     * 数据中心ID
     */
    private final long dataCenterId;

    private final Snowflake snowflake;

    public SnowflakeIdentityFactory(long workerId, long dataCenterId) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        snowflake = new Snowflake(workerId, dataCenterId);
    }

    @Override
    public Number create() {
        return snowflake.nextId();
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }
}