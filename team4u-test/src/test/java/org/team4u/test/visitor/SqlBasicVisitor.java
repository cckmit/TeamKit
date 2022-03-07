package org.team4u.test.visitor;

import org.team4u.test.visitor.node.*;

public class SqlBasicVisitor<R> extends SqlVisitor<R> {

    @Override
    public R visit(SelectNode selectNode) {
        selectNode.getFields().accept(this);
        selectNode.getWhere().accept(this);
        return null;
    }

    @Override
    public R visit(FieldsNode fieldsNode) {
        for (Expression field : fieldsNode.getFields()) {
            field.accept(this);
        }
        return null;
    }

    @Override
    public R visit(WhereNode whereNode) {
        for (Expression condition : whereNode.getConditions()) {
            condition.accept(this);
        }
        return null;
    }

    @Override
    public R visit(IdExpression idExpression) {
        return null;
    }

    @Override
    public R visit(FunctionCallExpression functionCallExpression) {
        for (Expression argument : functionCallExpression.getArguments()) {
            argument.accept(this);
        }
        return null;
    }

    @Override
    public R visit(OperatorExpression operatorExpression) {
        operatorExpression.getLeft().accept(this);
        operatorExpression.getRight().accept(this);
        return null;
    }

    @Override
    public R visit(LiteralExpression literalExpression) {
        return null;
    }
}