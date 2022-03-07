package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

@EqualsAndHashCode(callSuper = true)
@Data
public class IdExpression extends Expression {

    private final String id;

    public IdExpression(String id) {
        this.id = id;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}
