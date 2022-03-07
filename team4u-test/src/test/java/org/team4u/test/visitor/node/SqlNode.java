package org.team4u.test.visitor.node;

import org.team4u.test.visitor.SqlVisitor;

public abstract class SqlNode {
    // 用来接收访问者的方法
    public abstract <R> R accept(SqlVisitor<R> sqlVisitor);
}