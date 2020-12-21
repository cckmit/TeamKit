package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 流程定义不存在异常
 *
 * @author jay.wu
 */
public class ProcessDefinitionNotExistException extends DataNotExistException {

    public ProcessDefinitionNotExistException(String message) {
        super(message);
    }
}
