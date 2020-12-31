package org.team4u.workflow;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.selector.application.SelectorAppService;
import org.team4u.selector.domain.selector.entity.expression.ExpressionSelectorFactory;
import org.team4u.template.TemplateFunctionService;
import org.team4u.template.infrastructure.BeetlTemplateEngine;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.form.process.definition.node.AssigneeStaticNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;
import org.team4u.workflow.infrastructure.persistence.definition.JsonProcessDefinitionRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestUtil {

    public static final String TEST = "test";
    public static final String TEST1 = "test1";

    public static final ProcessDefinitionRepository processDefinitionRepository =
            new JsonProcessDefinitionRepository(new LocalJsonConfigService());

    public static ProcessInstance newInstance(String... assignees) {
        return new ProcessInstance(
                TEST,
                TEST,
                ProcessDefinitionId.of(TEST),
                null,
                TEST,
                null
        ).setAssignees(
                Optional.ofNullable(assignees)
                        .map(it -> Arrays.stream(assignees)
                                .map(assignee -> new ProcessAssignee(TEST, assignee))
                                .collect(Collectors.toSet()))
                        .orElse(Collections.emptySet())
        );
    }

    public static ProcessNodeHandlerContext.Builder contextBuilder() {
        return ProcessNodeHandlerContext.Builder
                .create()
                .withOperatorId(TEST)
                .withRemark(TEST);
    }

    public static ActionChoiceNode.ActionNode actionNode(String actionId, String nodeId) {
        return new ActionChoiceNode.ActionNode(actionId, nodeId);
    }

    public static ProcessFormAction action(String actionId, String... requiredPermissions) {
        return new ProcessFormAction(actionId, actionId, CollUtil.toList(requiredPermissions));
    }

    public static StaticNode staticNode(String nodeId) {
        return new StaticNode(nodeId, TEST, null);
    }

    public static StaticNode assigneeNode(String nodeId, String nextNodeId, String assignee) {
        return new AssigneeStaticNode(
                nodeId,
                nodeId,
                nextNodeId,
                AssigneeStaticNode.RULE_TYPE_USER,
                assignee);
    }

    public static ProcessDefinition definitionOf(String id) {
        return processDefinitionRepository.domainOf(id);
    }

    public static AssigneeActionChoiceNode anyAssigneeActionNode(ActionChoiceNode.ActionNode... actionNodes) {
        return new AssigneeActionChoiceNode(
                TEST,
                TEST,
                AssigneeActionChoiceNode.CHOICE_TYPE_ANY,
                Arrays.asList(actionNodes.clone()));
    }

    public static AssigneeActionChoiceNode allAssigneeActionNode(String choiceActionId,
                                                                 ActionChoiceNode.ActionNode... actionNodes) {
        return new AssigneeActionChoiceNode(
                TEST,
                TEST,
                AssigneeActionChoiceNode.CHOICE_TYPE_ALL,
                choiceActionId,
                Arrays.asList(actionNodes.clone()));
    }

    public static SelectorAppService selectorAppService() {
        return new SelectorAppService().registerSelectorFactory(
                new ExpressionSelectorFactory(
                        new BeetlTemplateEngine(new TemplateFunctionService())
                )
        );
    }
}
