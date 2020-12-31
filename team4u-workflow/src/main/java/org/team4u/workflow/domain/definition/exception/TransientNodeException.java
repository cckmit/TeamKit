package org.team4u.workflow.domain.definition.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 瞬时节点无法保存异常
 *
 * @author jay.wu
 */
public class TransientNodeException extends ProcessSystemException {

    public TransientNodeException(String message) {
        super(message);
    }
}