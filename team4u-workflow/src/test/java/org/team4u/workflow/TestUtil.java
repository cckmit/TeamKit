package org.team4u.workflow;

import cn.hutool.core.collection.CollUtil;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.definition.node.AssigneeNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;
import org.team4u.workflow.infrastructure.persistence.JsonProcessDefinitionRepository;
import org.team4u.workflow.infrastructure.persistence.LocalConfigService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ANY;

public class TestUtil {

    public static final String TEST = "test";
    public static final String TEST1 = "test1";

    public static final ProcessDefinitionRepository processDefinitionRepository = new JsonProcessDefinitionRepository(
            new LocalConfigService()
    );

    public static ProcessInstance newInstance(String... assignees) {
        return new ProcessInstance(
                TEST,
                TEST,
                TEST,
                null,
                TEST
        ).setAssignees(
                Optional.ofNullable(assignees).map(it -> {
                    return Arrays.stream(assignees)
                            .map(assignee -> new ProcessAssignee(assignee, assignee))
                            .collect(Collectors.toSet());
                }).orElse(Collections.emptySet())
        );
    }

    public static ProcessNodeHandler.Context context(ProcessInstance instance,
                                                     ProcessAction action,
                                                     ProcessDefinition definition,
                                                     ProcessNode node,
                                                     String... permissions) {
        return new ProcessNodeHandler.Context(
                instance,
                definition,
                action,
                TEST,
                new HashSet<>(CollUtil.toList(permissions)),
                TEST
        ).setNode(node);
    }

    public static ActionChoiceNode.ActionNode actionNode(String actionId, String nodeId) {
        return new ActionChoiceNode.ActionNode(actionId, nodeId);
    }

    public static ProcessAction action(String actionId, String... requiredPermissions) {
        return new ProcessAction(actionId, actionId, new HashSet<>(CollUtil.toList(requiredPermissions)));
    }

    public static StaticNode staticNode(String nodeId) {
        return new StaticNode(nodeId, TEST, null);
    }

    public static StaticNode assigneeNode(String nodeId, String nextNodeId, String assignee) {
        return new AssigneeNode(
                nodeId,
                nodeId,
                nextNodeId,
                AssigneeNode.RULE_TYPE_USER,
                assignee);
    }

    public static ProcessDefinition definitionOf(String id) {
        return processDefinitionRepository.domainOf(id);
    }

    public static AssigneeActionChoiceNode assigneeActionChoiceNode(ActionChoiceNode.ActionNode... actionNodes) {
        return new AssigneeActionChoiceNode(
                TEST,
                TEST,
                CHOICE_TYPE_ANY,
                Arrays.asList(actionNodes.clone())
        );
    }
}