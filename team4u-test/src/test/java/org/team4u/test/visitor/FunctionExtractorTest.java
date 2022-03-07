package org.team4u.test.visitor;

import org.junit.Test;
import org.team4u.test.visitor.node.*;

import java.util.Arrays;
import java.util.Collections;

public class FunctionExtractorTest {

    @Test
    public void visit() {
        // sql 定义
        SqlNode sql = new SelectNode( //select
                // concat("test-", upper(name))
                new FieldsNode(Collections.singletonList(
                        new FunctionCallExpression("concat", Arrays.asList(
                                new LiteralExpression("test-"),
                                new FunctionCallExpression(
                                        "upper",
                                        Collections.singletonList(new IdExpression("name"))
                                )
                        ))
                )),
                // from test
                Collections.singletonList("test"),
                // where age > 20
                new WhereNode(Collections.singletonList(new OperatorExpression(
                        new IdExpression("age"),
                        ">",
                        new LiteralExpression("20")
                )))
        );
        // 使用 FunctionExtractor
        FunctionExtractor functionExtractor = new FunctionExtractor();
        sql.accept(functionExtractor);
        System.out.println(functionExtractor.getFunctions());
    }
}
