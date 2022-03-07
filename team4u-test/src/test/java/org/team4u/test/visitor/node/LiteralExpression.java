package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

@EqualsAndHashCode(callSuper = true)
@Data
public class LiteralExpression extends Expression {

    private final String literal;

    public LiteralExpression(String literal) {
        this.literal = literal;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}
