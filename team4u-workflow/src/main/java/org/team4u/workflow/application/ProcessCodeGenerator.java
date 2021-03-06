package org.team4u.workflow.application;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Dict;
import org.team4u.template.TemplateEngine;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.node.StaticNode;

import java.util.stream.Collectors;

/**
 * 流程代码生成器
 *
 * @author jay.wu
 */
public class ProcessCodeGenerator {

    private final Config config;
    private final TemplateEngine templateEngine;

    public ProcessCodeGenerator(Config config,
                                TemplateEngine templateEngine) {
        this.config = config;
        this.templateEngine = templateEngine;
    }

    /**
     * 生成静态节点枚举类
     */
    public String enumCodeForStaticNodes(ProcessDefinition definition) {
        return templateEngine.render(
                ResourceUtil.readUtf8Str(config.getNodesTemplatePath()),
                Dict.create().set("nodes", definition.getNodes()
                        .stream()
                        .filter(it -> it instanceof StaticNode)
                        .collect(Collectors.toList()))
                        .set("className", definition.getProcessDefinitionId().getId() + "ProcessNode")
        );
    }

    /**
     * 生成动作枚举类
     */
    public String enumCodeForActions(ProcessDefinition definition) {
        return templateEngine.render(
                ResourceUtil.readUtf8Str(config.getActionsTemplatePath()),
                Dict.create().set("actions", definition.getActions())
                        .set("className", definition.getProcessDefinitionId().getId() + "ProcessAction")
        );
    }

    public static class Config {

        private String nodesTemplatePath = "code/nodes_template.txt";
        private String actionsTemplatePath = "code/actions_template.txt";

        public String getNodesTemplatePath() {
            return nodesTemplatePath;
        }

        public Config setNodesTemplatePath(String nodesTemplatePath) {
            this.nodesTemplatePath = nodesTemplatePath;
            return this;
        }

        public String getActionsTemplatePath() {
            return actionsTemplatePath;
        }

        public Config setActionsTemplatePath(String actionsTemplatePath) {
            this.actionsTemplatePath = actionsTemplatePath;
            return this;
        }
    }
}