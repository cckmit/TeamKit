package org.team4u.workflow.domain.emulator;

import org.team4u.base.error.DataNotExistException;

/**
 * 流程模拟器脚本不存在异常
 *
 * @author jay.wu
 */
public class ProcessEmulatorScriptNotExistException extends DataNotExistException {

    public ProcessEmulatorScriptNotExistException(String message) {
        super(message);
    }
}
