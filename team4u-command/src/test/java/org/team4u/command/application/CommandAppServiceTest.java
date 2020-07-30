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

        CommandConfig commandConfig = new CommandConfig(commandId, null);
        MockCommandRequest commandRequest = new MockCommandRequest(commandId);
        MockCommandResponse commandResponse = new MockCommandResponse();

        Mockito.when(configRepository.configOf(commandId)).thenReturn(commandConfig);
        Mockito.when(commandExecutor.execute(commandConfig, commandRequest)).thenReturn(commandResponse);

        CommandAppService appService = new CommandAppService(commandExecutor, configRepository);
        MockCommandResponse acr = appService.execute(commandId, commandRequest);

        Assert.assertEquals(commandResponse, acr);
    }
}