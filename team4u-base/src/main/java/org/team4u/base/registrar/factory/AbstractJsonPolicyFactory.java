package org.team4u.base.registrar.factory;

import cn.hutool.json.JSONUtil;

/**
 * 基于Json配置的抽象策略工厂
 *
 * @param <C> 配置类型
 * @param <P> 策略类型
 * @author jay.wu
 */
public abstract class AbstractJsonPolicyFactory<C, P> extends AbstractPolicyFactory<C, String, P> {

    @Override
    protected C toConfig(String configValue) {
        return JSONUtil.toBean(configValue, configType());
    }
}