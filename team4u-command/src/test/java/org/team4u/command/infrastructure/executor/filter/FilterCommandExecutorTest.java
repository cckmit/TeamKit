package org.team4u.command.infrastructure.executor.filter;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.filter.v2.Filter;
import org.team4u.base.filter.v2.FilterChain;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogHandler;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

public class FilterCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        return new FilterCommandExecutor(CollUtil.newArrayList(new FilterCommandExecutorTest.MockRoutersBuilder()));
    }

    public static class MockRoutersBuilder implements CommandRoutesBuilder {

        private final Filter<CommandHandler.Context> commandRequester;
        private final Filter<CommandHandler.Context> commandLogHandler;

        public MockRoutersBuilder() {
            this.commandRequester = CommandFilterBuilder.filter(new MockHttpCommandRequester());
            this.commandLogHandler = CommandFilterBuilder.filter(new MockCommandLogHandler());
        }

        @Override
        public FilterChain.Config configure() {
            return FilterChain.Config.builder()
                    .filter(commandLogHandler)
                    .filter(commandRequester)
                    .filter(commandLogHandler)
                    .build();
        }

        @Override
        public String id() {
            return "test";
        }
    }
}