package org.team4u.command.infrastructure.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.team4u.command.TestUtil;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.infrastructure.executor.MockCommandResponse;
import org.team4u.command.infrastructure.util.ExtractConfig;

@RunWith(MockitoJUnitRunner.class)
public class JsonCommandConfigRepositoryTest {

    @Test
    public void configOf() {
        CommandConfig commandConfig = TestUtil.configOf("test_command.json");

        ExtractConfig extractConfig = commandConfig.itemOf(
                "jsonExtractor",
                ExtractConfig.class
        );

        Assert.assertEquals(MockCommandResponse.class.getName(), extractConfig.getTargetType());
        Assert.assertEquals("{\"channelCode\":\"x\"}", extractConfig.getTemplate());
    }
}