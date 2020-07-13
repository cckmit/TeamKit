package org.team4u.base.id;

import cn.hutool.core.util.IdUtil;

/**
 * 基于MongoDB ID算法的主键生成器
 *
 * @author jay.wu
 */
public class ObjectIdentityFactory implements StringIdentityFactory {

    @Override
    public String create() {
        return IdUtil.objectId();
    }
}