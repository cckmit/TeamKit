package org.team4u.command.infrastructure.executor.liteflow;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeCondComponent;
import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.util.SpringAware;
import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 节点组件服务
 *
 * @author jay.wu
 */
public class NodeComponents {

    public static NodeComponent registerNode(String name, CommandHandler handler) {
        NodeComponent c = new NodeComponent() {

            @Override
            public void process() {
                handler.handle(getSlot().getRequestData());
            }
        };

        registerComponent(name, c);
        return c;
    }

    public static NodeCondComponent registerCond(String name, CommandHandler handler) {
        NodeCondComponent c = new NodeCondComponent() {
            @Override
            public String processCond() {
                CommandHandler.Context context = getSlot().getRequestData();
                handler.handle(getSlot().getRequestData());
                return context.extraAttribute(LiteflowConstant.NODE_COND_KEY);
            }
        };

        registerComponent(name, c);
        return c;
    }

    public static void registerComponent(String name, NodeComponent component) {
        SpringAware.getBean(ComponentScanner.class).postProcessAfterInitialization(component, name);
    }
}