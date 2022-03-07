package org.team4u.test.visitor.node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.test.visitor.SqlVisitor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SelectNode extends SqlNode {

    private final FieldsNode fields;

    private final List<String> from;

    private final WhereNode where;

    public SelectNode(FieldsNode fields, List<String> from, WhereNode where) {
        this.fields = fields;
        this.from = from;
        this.where = where;
    }

    @Override
    public <R> R accept(SqlVisitor<R> sqlVisitor) {
        return sqlVisitor.visit(this);
    }
}