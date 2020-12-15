package org.team4u.workflow;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.ProcessAssignee;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.team4u.workflow.domain.definition.node.AssigneeActionChoiceNode.CHOICE_TYPE_ANY;

public class TestUtil {

    public static final String TEST = "test";
    public static final String TEST1 = "test1";

    public static ProcessInstance newInstance(String... assignees) {
        return new ProcessInstance(
                TEST,
                null,
                null,
                null,
                null
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
                                                     ProcessNode node) {
        return new ProcessNodeHandler.Context(
                instance,
                definition,
                action,
                TEST,
                TEST
        ).setNode(node);
    }

    public static ActionChoiceNode.ActionNode actionNode(String actionId, String nodeId) {
        return new ActionChoiceNode.ActionNode(action(actionId), staticNode(nodeId));
    }

    public static ProcessAction action(String actionId) {
        return new ProcessAction().setActionId(actionId).setActionName(actionId);
    }

    public static StaticNode staticNode(String nodeId) {
        return new StaticNode(nodeId, TEST, null);
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
