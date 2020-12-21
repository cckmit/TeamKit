package org.team4u.workflow.domain.form.process.definition.node;

import org.team4u.workflow.domain.definition.node.ActionChoiceNode;

import java.util.List;

/**
 * 处理人动作选择器节点
 *
 * @author jay.wu
 */
public class AssigneeActionChoiceNode extends ActionChoiceNode {

    public static final String CHOICE_TYPE_ANY = "ANY";

    /**
     * 选择类型
     */
    private final String choiceType;

    public AssigneeActionChoiceNode(String nodeId,
                                    String nodeName,
                                    String choiceType,
                                    List<ActionNode> actionNodes) {
        super(nodeId, nodeName, actionNodes);
        this.choiceType = choiceType;
    }

    public String getChoiceType() {
        return choiceType;
    }
}