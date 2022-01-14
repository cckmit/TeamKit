package org.team4u.workflow.infrastructure.persistence.instance.ext;

import org.apache.ibatis.type.TypeHandler;
import org.team4u.base.registrar.Policy;

/**
 * 扩展处理器
 *
 * @author jay.wu
 */
public interface ExtTypeHandler extends TypeHandler<String>, Policy<Void> {

    @Override
    default boolean supports(Void context) {
        return true;
    }
}