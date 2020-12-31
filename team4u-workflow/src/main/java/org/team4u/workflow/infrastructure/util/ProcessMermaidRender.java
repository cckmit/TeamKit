package org.team4u.workflow.infrastructure.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.selector.domain.selector.entity.expression.ExpressionSelectorFactory;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程定义Mermaid渲染器
 *
 * @author jay.wu
 * @link https://mermaid-js.github.io/mermaid/#/flowchart
 */
public class ProcessMermaidRender {

    private final ProcessDefinition definition;

    public ProcessMermaidRender(ProcessDefinition definition) {
        this.definition = definition;
    }

    public String render() {
        MermaidFlow flow = new MermaidFlow();

        for (ProcessNode node : definition.getNodes()) {
            if (node instanceof StaticNode) {
                toElement(flow, (StaticNode) node);
            } else if (node instanceof ActionChoiceNode) {
                toElement(flow, (ActionChoiceNode) node);
            } else if (node instanceof DynamicChoiceNode) {
                toElement(flow, (DynamicChoiceNode) node);
            } else if (node instanceof BeanChoiceNode) {
                toElement(flow, (BeanChoiceNode) node);
            } else if (node instanceof BeanProcessingNode) {
                toElement(flow, (BeanProcessingNode) node);
            } else {
                toElement(flow, node);
            }
        }

        return flow.toString();
    }

    protected MermaidFlow.Node toFlowNode(String nodeId) {
        return toFlowNode(definition.processNodeOf(nodeId));
    }

    protected MermaidFlow.Node toFlowNode(ProcessNode node) {
        MermaidFlow.Node flowNode;

        if (node instanceof StaticNode) {
            flowNode = new MermaidFlow.Square(node.getNodeId(), node.getNodeName());
        } else if (node instanceof BeanProcessingNode) {
            flowNode = new MermaidFlow.RoundEdges(node.getNodeId(), node.getNodeName());
        } else {
            flowNode = new MermaidFlow.Rhombus(node.getNodeId(), node.getNodeName());
        }

        return flowNode;
    }

    protected void toElement(MermaidFlow flow, ProcessNode node) {
        flow.getElements().add(toFlowNode(node));
    }

    protected void toElement(MermaidFlow flow, BeanProcessingNode node) {
        if (node.getNextNodeId() == null) {
            return;
        }

        flow.getElements().add(newLink(
                toFlowNode(node),
                node.getNextNodeId(),
                null
        ));
    }

    protected void toElement(MermaidFlow flow, BeanChoiceNode node) {
        if (node.getNextNodeIds() == null) {
            return;
        }

        for (String nextNodeId : node.getNextNodeIds()) {
            flow.getElements().add(newLink(
                    node.getNodeId(),
                    nextNodeId,
                    null
            ));
        }
    }

    protected void toElement(MermaidFlow flow, DynamicChoiceNode node) {
        switch (node.getRule().getType()) {
            case "expression": {
                Map<String, String> config = ExpressionSelectorFactory.toConfig(node.getRule().getBody());
                for (Map.Entry<String, String> entry : config.entrySet()) {
                    flow.getElements().add(newLink(
                            node.getNodeId(),
                            entry.getKey(),
                            "\"" + entry.getValue() + "\""
                    ));
                }
                break;
            }

            default:
                break;
        }
    }

    protected void toElement(MermaidFlow flow, StaticNode node) {
        if (node.getNextNodeId() == null) {
            return;
        }

        flow.getElements().add(newLink(
                toFlowNode(node.getNodeId()),
                node.getNextNodeId(),
                null
        ));
    }

    protected void toElement(MermaidFlow flow, ActionChoiceNode node) {
        for (ActionChoiceNode.ActionNode actionNode : node.getActionNodes()) {
            String actionName = definition.availableActionOf(actionNode.getActionId()).getActionName();

            flow.getElements().add(newLink(node.getNodeId(), actionNode.getNextNodeId(), actionName));
        }
    }

    protected MermaidFlow.Link newLink(String fromNodeId,
                                       String toNodeId,
                                       String text) {
        MermaidFlow.Node fromFlowNode = new MermaidFlow.Node(fromNodeId);
        return newLink(fromFlowNode, toNodeId, text);
    }

    protected MermaidFlow.Link newLink(MermaidFlow.Node fromFlowNode,
                                       String toNodeId,
                                       String text) {

        ProcessNode toNode = definition.processNodeOf(toNodeId);
        MermaidFlow.Node toFlowNode = toFlowNode(toNode);

        if (!(toNode instanceof ActionChoiceNode)) {
            return new MermaidFlow.ArrowLink(
                    fromFlowNode,
                    toFlowNode,
                    text
            );
        }

        if (toNode.getClass().equals(BeanActionChoiceNode.class)) {
            return new MermaidFlow.ArrowLink(
                    fromFlowNode,
                    toFlowNode,
                    text
            );
        } else {
            return new MermaidFlow.DottedLink(
                    fromFlowNode,
                    toFlowNode,
                    text
            );
        }
    }

    public static class MermaidFlow {

        private Direction direction = Direction.TD;
        private List<Element> elements = new ArrayList<>();

        private String renderElements() {
            StringBuilder buffer = new StringBuilder();

            for (Element element : elements) {
                buffer.append("    ")
                        .append(element.toString())
                        .append("\n");
            }

            return buffer.toString();
        }

        @Override
        public String toString() {
            return String.format(
                    "graph %s\n%s",
                    getDirection().name(),
                    renderElements()
            );
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }

        /**
         * 流程图方向
         */
        public enum Direction {
            /**
             * 纵向
             */
            TD,
            /**
             * 横向
             */
            LR
        }

        public interface Element {

        }


        public static class Node implements Element {

            private final String id;
            private final String text;

            public Node(String id) {
                this(id, null);
            }

            public Node(String id, String text) {
                this.id = id;
                this.text = text;
            }

            public String getId() {
                return id;
            }

            public String getText() {
                return text;
            }

            @Override
            public String toString() {
                return ObjectUtil.defaultIfBlank(getText(), getId());
            }
        }

        /**
         * 方形
         */
        public static class Square extends Node {

            public Square(String id, String text) {
                super(id, text);
            }

            @Override
            public String toString() {
                return String.format("%s[%s]", getId(), getText());
            }
        }

        public static class RoundEdges extends Node {

            public RoundEdges(String id, String text) {
                super(id, text);
            }

            @Override
            public String toString() {
                return String.format("%s(%s)", getId(), getText());
            }
        }

        public static class Rhombus extends Node {

            public Rhombus(String id, String text) {
                super(id, text);
            }

            @Override
            public String toString() {
                return String.format("%s{%s}", getId(), getText());
            }
        }

        public static abstract class Link implements Element {

            private final Node fromNode;
            private final Node toNode;
            private final String text;

            public Link(Node fromNode, Node toNode, String text) {
                this.fromNode = fromNode;
                this.toNode = toNode;
                this.text = text;
            }

            @Override
            public String toString() {
                return getFromNode().toString() + toRelated() + getToNode().toString();
            }

            protected abstract String toRelated();

            public Node getFromNode() {
                return fromNode;
            }

            public Node getToNode() {
                return toNode;
            }

            public String getText() {
                return text;
            }
        }

        public static class ArrowLink extends Link {

            public ArrowLink(Node fromNode, Node toNode) {
                this(fromNode, toNode, null);
            }

            public ArrowLink(Node fromNode, Node toNode, String text) {
                super(fromNode, toNode, text);
            }

            @Override
            protected String toRelated() {
                String arrow = "-->";

                if (StrUtil.isNotBlank(getText())) {
                    return String.format("%s|%s|", arrow, getText());
                }

                return arrow;
            }
        }

        public static class DottedLink extends Link {

            public DottedLink(Node fromNode, Node toNode) {
                this(fromNode, toNode, null);
            }

            public DottedLink(Node fromNode, Node toNode, String text) {
                super(fromNode, toNode, text);
            }

            @Override
            protected String toRelated() {
                if (StrUtil.isNotBlank(getText())) {
                    return String.format("-. %s .->", getText());
                }

                return "-.->";
            }
        }
    }
}