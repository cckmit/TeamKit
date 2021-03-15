package org.team4u.command.infrastructure.executor;

import org.team4u.command.domain.executor.handler.log.CommandLogHandler;

public class MockCommandLogHandler extends CommandLogHandler {

    public MockCommandLogHandler() {
        super(new MockCommandLogRepository());
    }
}
