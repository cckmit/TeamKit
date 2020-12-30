package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.SystemException;

/**
 * 非静态流程节点异常
 *
 * @author jay.wu
 */
public class NotStaticNodeException extends SystemException {

    public NotStaticNodeException(String message) {
        super(message);
    }
}