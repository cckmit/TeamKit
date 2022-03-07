package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FieldsNode extends SqlNode {

    private final List<Expression> fields;

    public FieldsNode(List<Expression> fields) {
        this.fields = fields;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}