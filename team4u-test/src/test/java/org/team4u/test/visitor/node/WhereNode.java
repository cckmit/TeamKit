package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WhereNode extends SqlNode {
    private final List<Expression> conditions;

    public WhereNode(List<Expression> conditions) {
        this.conditions = conditions;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}
