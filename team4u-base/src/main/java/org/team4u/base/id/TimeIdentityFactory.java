package org.team4u.base.id;


/**
 * 基于时间戳的id生成器
 *
 * @author jay.wu
 */
public class TimeIdentityFactory implements IdentityFactory<Long> {

    @Override
    public Long create() {
        return System.currentTimeMillis();
    }
}