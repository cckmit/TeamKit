package org.team4u.command.infrastructure.executor.camel;


import org.apache.camel.RoutesBuilder;
import org.team4u.base.lang.IdObject;

/**
 * 命令路由构建器
 *
 * @author jay.wu
 */
public interface CommandRoutesBuilder extends RoutesBuilder, IdObject<String> {
}
