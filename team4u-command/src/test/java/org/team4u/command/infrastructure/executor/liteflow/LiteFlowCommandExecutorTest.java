package org.team4u.command.infrastructure.executor.liteflow;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.util.SpringAware;
import org.junit.BeforeClass;
import org.springframework.context.support.StaticApplicationContext;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.infrastructure.executor.AbstractCommandExecutorTest;
import org.team4u.command.infrastructure.executor.MockCommandLogHandler;
import org.team4u.command.infrastructure.executor.MockHttpCommandRequester;

import java.util.List;

public class LiteFlowCommandExecutorTest extends AbstractCommandExecutorTest {

    private final static StaticApplicationContext applicationContext = new StaticApplicationContext();

    @BeforeClass
    public static void beforeClass() {
        new SpringAware().setApplicationContext(applicationContext);
        applicationContext.registerBean(ComponentScanner.class);
    }

    @Override
    protected CommandExecutor commandExecutor() {
        return new LiteFlowCommandExecutor(CollUtil.newArrayList(new CommandRoutesBuilder() {
            @Override
            public void registerNodes() {
                NodeComponents.registerNode("commandRequester", new MockHttpCommandRequester());
                NodeComponents.registerNode("commandLogHandler", new MockCommandLogHandler());
            }

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