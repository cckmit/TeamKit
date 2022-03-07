package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorExpression extends Expression {

    private final Expression left;

    private final String operator;

    private final Expression right;

    public OperatorExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}