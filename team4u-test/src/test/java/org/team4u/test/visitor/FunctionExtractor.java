package org.team4u.test.visitor;

import lombok.Getter;
import org.team4u.test.visitor.node.FunctionCallExpression;

import java.util.ArrayList;
import java.util.List;

public class FunctionExtractor extends SqlBasicVisitor<Void> {

    @Getter
    private final List<String> functions = new ArrayList<>();

    @Override
    public Void visit(FunctionCallExpression functionCallExpression) {
        functions.add(functionCallExpression.getName());
        return super.visit(functionCallExpression);
    }
}