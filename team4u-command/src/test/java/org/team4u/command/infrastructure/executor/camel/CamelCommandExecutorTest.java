package org.team4u.command.infrastructure.executor.camel;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandLogHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogRepository;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

public class CamelCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        MockRoutersBuilder mockRoutersBuilder = new MockRoutersBuilder();
        return new CamelCommandExecutor(CollUtil.newArrayList(mockRoutersBuilder));
    }

    public static class MockRoutersBuilder extends AbstractTraceableCommandRoutesBuilder {

        public MockRoutersBuilder() {
            super(
                    new MockHttpCommandRequester(null),
                    new CommandLogHandler(new MockCommandLogRepository())
            );
        }

        @Override
        public String id() {
            return "test";
        }
    }
}