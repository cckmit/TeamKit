package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.filter.Filter;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogRepository;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;
import org.team4u.command.infrastructure.executor.MockHttpRequester;

import java.util.List;

public class SimpleCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        return new SimpleCommandExecutor(CollUtil.newArrayList(new MockRoutersBuilder()));
    }

    public static class MockRoutersBuilder implements CommandRoutesBuilder {

        private final Filter<CommandHandler.Context> commandRequester;
        private final Filter<CommandHandler.Context> commandLogHandler;

        public MockRoutersBuilder() {
            this.commandRequester = CommandFilterBuilder.sequentialFilterOf(
                    new MockHttpCommandRequester(new MockHttpRequester()));
            this.commandLogHandler = CommandFilterBuilder.sequentialFilterOf(
                    new CommandLogHandler(new MockCommandLogRepository())
            );
        }

        @Override
        public List<Filter<CommandHandler.Context>> configure() {
            return CollUtil.newArrayList(
                    commandLogHandler,
                    commandRequester,
                    commandLogHandler
            );
        }

        @Override
        public String id() {
            return "test";
        }
    }
}