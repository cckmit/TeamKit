package org.team4u.command.application;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.config.CommandConfigRepository;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.infrastructure.executor.MockCommandRequest;
import org.team4u.command.infrastructure.executor.MockCommandResponse;

@RunWith(MockitoJUnitRunner.class)
public class CommandAppServiceTest {

    @Mock
    private CommandExecutor commandExecutor;
    @Mock
    private CommandConfigRepository configRepository;

    @Test
    public void execute() {
        String commandId = "test";

        CommandConfig commandConfig = new CommandConfig(null);
        MockCommandRequest commandRequest = new MockCommandRequest(commandId);
        MockCommandResponse commandResponse = new MockCommandResponse();

        MockCommandResponse acr = appService(
                commandId,
                commandConfig,
                commandRequest,
                commandResponse
        ).execute(commandId, commandRequest);

        Assert.assertEquals(commandResponse, acr);
    }

    private CommandAppService appService(String commandId,
                                         CommandConfig config,
                                         MockCommandRequest request,
                                         MockCommandResponse response) {
        Mockito.when(configRepository.configOfId(commandId)).thenReturn(config);
        Mockito.when(commandExecutor.execute(commandId, config, request)).thenReturn(response);

        return new CommandAppService(commandExecutor, configRepository);
    }
}