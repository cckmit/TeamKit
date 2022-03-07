package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FunctionCallExpression extends Expression {

    private final String name;

    private final List<Expression> arguments;

    public FunctionCallExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}
