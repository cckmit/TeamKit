package org.team4u.command.infrastructure.executor;

import org.team4u.command.domain.executor.CommandLog;
import org.team4u.command.domain.executor.CommandLogRepository;


public class MockCommandLogRepository implements CommandLogRepository {

    private MockCommandRequest request;
    private MockCommandResponse response;

    @Override
    public CommandLog logOf(String commandLogId) {
        return null;
    }

    @Override
    public void save(CommandLog commandLog) {
        this.request = commandLog.getRequest();

        if (commandLog.getResponse() != null) {
            MockCommandResponse mr = commandLog.getResponse();
            this.response = mr.setChannelRawBody(request.getName());
        }
    }

    public MockCommandRequest getRequest() {
        return request;
    }

    public MockCommandResponse getResponse() {
        return response;
    }
}
