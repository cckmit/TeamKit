package org.team4u.command.infrastructure.executor.camel;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.handler.CommandLogHandler;

import java.util.HashMap;

public class CamelCommandExecutorTest {

    @Test
    public void execute() {
        MockRoutersBuilder mockRoutersBuilder = new MockRoutersBuilder();
        CamelCommandExecutor service = new CamelCommandExecutor(CollUtil.newArrayList(mockRoutersBuilder));

        MockCommandResponse response = (MockCommandResponse) service.execute(
                new CommandConfig(new HashMap<>()),
                new CommandRequest(null, null, "test") {
                });

        Assert.assertEquals("test", response.getChannelCode());
        Assert.assertEquals("test", response.getChannelRawBody());
    }

    public static class MockRoutersBuilder extends AbstractTraceableCommandRoutesBuilder {

        public MockRoutersBuilder() {
            super(
                    new MockHttpCommandHandler(null),
                    new CommandLogHandler(new MockCommandLogRepository())
            );
        }

        @Override
        public String id() {
            return "test";
        }
    }
}
