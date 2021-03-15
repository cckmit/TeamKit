package org.team4u.command.infrastructure.executor.liteflow;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.monitor.MonitorBus;
import com.yomahub.liteflow.spring.ComponentScaner;
import com.yomahub.liteflow.util.SpringAware;
import org.springframework.context.support.StaticApplicationContext;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogHandler;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

import java.util.List;

public class LiteflowCommandExecutorTest extends AbstractCommandExecutorTest {

    private final StaticApplicationContext applicationContext = new StaticApplicationContext();

    @Override
    protected CommandExecutor commandExecutor() {
        new SpringAware().setApplicationContext(applicationContext);
        applicationContext.registerBean(MonitorBus.class, false);
        applicationContext.registerBean(ComponentScaner.class);

        NodeComponentBuilder.nodeOf("commandRequester", new MockHttpCommandRequester());
        NodeComponentBuilder.nodeOf("commandLogHandler", new MockCommandLogHandler());

        return new LiteflowCommandExecutor(CollUtil.newArrayList(new CommandRoutesBuilder() {
            @Override
            public List<String> configure() {
                return CollUtil.newArrayList("liteflow_test1.xml");
            }

            @Override
            public String id() {
                return "test";
            }
        }));
    }
}