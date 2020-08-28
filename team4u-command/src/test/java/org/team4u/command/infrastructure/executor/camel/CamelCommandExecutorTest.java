package org.team4u.command.infrastructure.executor.camel;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogRepository;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;
import org.team4u.command.infrastructure.executor.MockHttpRequester;

public class CamelCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        MockRoutersBuilder mockRoutersBuilder = new MockRoutersBuilder();
        return new CamelCommandExecutor(CollUtil.newArrayList(mockRoutersBuilder));
    }

    public static class MockRoutersBuilder extends AbstractTraceableCommandRoutesBuilder {

        public MockRoutersBuilder() {
            super(
                    new MockHttpCommandRequester(new MockHttpRequester()),
                    new CommandLogHandler(new MockCommandLogRepository())
            );
        }

        @Override
        public String id() {
            return "test";
        }
    }
}