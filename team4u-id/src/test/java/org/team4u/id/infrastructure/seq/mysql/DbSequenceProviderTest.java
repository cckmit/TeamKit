package org.team4u.id.infrastructure.seq.mysql;

import org.junit.Test;
import org.team4u.test.spring.SpringDbTest;

public class DbSequenceProviderTest extends SpringDbTest {

    @Test
    public void provide() {
    }

    @Override
    protected String[] ddlResourcePaths() {
        return new String[0];
    }
}