package org.team4u.command.infrastructure.executor;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandExecutor;

import java.util.HashMap;

public abstract class AbstractCommandExecutorTest {

    @Test
    public void execute() {
        MockCommandResponse response = (MockCommandResponse) commandExecutor().execute(
                "test",
                new CommandConfig(new HashMap<>()),
                new MockCommandRequest("test")
        );

        Assert.assertEquals("test", response.getChannelCode());
        Assert.assertEquals("test", response.getChannelRawBody());
    }

    protected abstract CommandExecutor commandExecutor();
}