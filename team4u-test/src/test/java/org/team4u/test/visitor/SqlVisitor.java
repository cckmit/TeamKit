package org.team4u.test.visitor;

import org.team4u.test.visitor.node.*;

public abstract class SqlVisitor<R> {
    public abstract R visit(SelectNode selectNode);

    public abstract R visit(FieldsNode fieldsNode);

    public abstract R visit(WhereNode whereNode);

    public abstract R visit(IdExpression idExpression);

    public abstract R visit(FunctionCallExpression functionCallExpression);

    public abstract R visit(OperatorExpression operatorExpression);

    public abstract R visit(LiteralExpression literalExpression);
}