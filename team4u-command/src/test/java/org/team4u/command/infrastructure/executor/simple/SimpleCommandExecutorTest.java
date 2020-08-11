package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogRepository;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

public class SimpleCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        return new SimpleCommandExecutor(CollUtil.newArrayList(new MockRoutersBuilder()));
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