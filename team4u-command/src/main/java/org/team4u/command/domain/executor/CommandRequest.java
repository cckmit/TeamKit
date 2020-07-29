package org.team4u.command.domain.executor;

/**
 * 标准请求
 *
 * @author jay.wu
 */
public class CommandRequest {
    /**
     * 请求者标识
     */
    private final String requesterId;
    /**
     * 请求标识
     */
    private final String requestId;
    /**
     * 命令标识
     */
    private final String commandId;

    public CommandRequest(String requesterId, String requestId, String commandId) {
        this.requesterId = requesterId;
        this.requestId = requestId;
        this.commandId = commandId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCommandId() {
        return commandId;
    }
}