package org.team4u.base.id;

import cn.hutool.core.util.IdUtil;

/**
 * uuid主键生成器
 *
 * @author jay.wu
 */
public class UuidIdentityFactory implements StringIdentityFactory {

    @Override
    public String create() {
        return IdUtil.simpleUUID();
    }
}