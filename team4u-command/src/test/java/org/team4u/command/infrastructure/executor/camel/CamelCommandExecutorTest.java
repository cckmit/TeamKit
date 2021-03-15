package org.team4u.command.infrastructure.executor.camel;

import cn.hutool.core.collection.CollUtil;
import org.apache.camel.builder.RouteBuilder;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogHandler;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

public class CamelCommandExecutorTest extends AbstractCommandExecutorTest {

    @Override
    protected CommandExecutor commandExecutor() {
        MockRoutersBuilder mockRoutersBuilder = new MockRoutersBuilder();
        return new CamelCommandExecutor(CollUtil.newArrayList(mockRoutersBuilder));
    }

    public static class MockRoutersBuilder extends RouteBuilder implements CommandRoutesBuilder {

        private final CommandLogHandler commandLogHandler;
        private final MockHttpCommandRequester commandRequester;

        public MockRoutersBuilder() {
            commandRequester = new MockHttpCommandRequester();
            commandLogHandler = new MockCommandLogHandler();
        }

        @Override
        public void configure() {
            bindToRegistry();

            from(startUri())
                    .to("bean:commandLogHandler?method=handle")
                    .to("bean:commandRequester?method=handle")
                    .to("bean:commandLogHandler?method=handle");
        }

        private void bindToRegistry() {
            bindToRegistry("commandRequester", commandRequester);
            bindToRegistry("commandLogHandler", commandLogHandler);
        }

        protected String startUri() {
            return "direct:" + id();
        }

        @Override
        public String id() {
            return "test";
        }
    }
}