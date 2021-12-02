package org.team4u.id.application;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.config.LocalJsonConfigService;
import org.team4u.id.infrastructure.seq.JsonSequenceConfigRepository;

public class SequenceGeneratorTest {

    @Test
    public void next() {
        SequenceGenerator g = new SequenceGenerator(new JsonSequenceConfigRepository(new LocalJsonConfigService()));
        Assert.assertEquals(1, g.next("seq_test").intValue());
        Assert.assertEquals(1, g.next("seq_test_with_group").intValue());
    }
}