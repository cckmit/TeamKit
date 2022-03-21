package org.team4u.command.infrastructure.executor.filter;


import org.team4u.base.filter.v2.FilterChain;
import org.team4u.base.registrar.StringIdPolicy;

/**
 * 命令路由构建器
 *
 * @author jay.wu
 */
public interface CommandRoutesBuilder extends StringIdPolicy {

    /**
     * 配置处理器路由
     *
     * @return 过滤器配置
     */
    FilterChain.Config configure();
}