package org.team4u.command.infrastructure.executor.liteflow;

import cn.hutool.core.util.ReflectUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.core.NodeCondComponent;
import com.yomahub.liteflow.monitor.MonitorBus;
import com.yomahub.liteflow.spring.ComponentScaner;
import com.yomahub.liteflow.util.SpringAware;
import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 节点构建器
 *
 * @author jay.wu
 */
public class NodeComponentBuilder {

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

    private static void registerComponent(String name, NodeComponent component) {
        ReflectUtil.setFieldValue(component, "monitorBus", SpringAware.getBean(MonitorBus.class));
        SpringAware.getBean(ComponentScaner.class).postProcessBeforeInitialization(component, name);
    }
}