package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 流程定义标识非法异常
 *
 * @author jay.wu
 */
public class ProcessDefinitionIdIllegalException extends DataNotExistException {

    public ProcessDefinitionIdIllegalException(String message) {
        super(message);
    }
}
