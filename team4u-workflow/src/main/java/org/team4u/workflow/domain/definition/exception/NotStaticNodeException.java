package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 非静态流程节点异常
 *
 * @author jay.wu
 */
public class NotStaticNodeException extends DataNotExistException {

    public NotStaticNodeException(String message) {
        super(message);
    }
}