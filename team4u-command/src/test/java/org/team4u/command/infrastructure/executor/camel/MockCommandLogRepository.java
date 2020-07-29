package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.executor.CommandLog;
import org.team4u.command.domain.executor.CommandLogRepository;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;


public class MockCommandLogRepository implements CommandLogRepository {

    private CommandRequest request;
    private MockCommandResponse response;

    @Override
    public CommandLog find(String commandLogId) {
        return null;
    }

    @Override
    public void save(CommandLog commandLog) {
        this.request = commandLog.getRequest();

        if (commandLog.getResponse() != null) {
            MockCommandResponse mr = (MockCommandResponse) commandLog.getResponse();
            this.response = mr.setChannelRawBody(request.getCommandId());
        }
    }

    public CommandRequest getRequest() {
        return request;
    }

    public CommandResponse getResponse() {
        return response;
    }
}
