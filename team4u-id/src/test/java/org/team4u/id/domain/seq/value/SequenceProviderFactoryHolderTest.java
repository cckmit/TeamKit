package org.team4u.id.domain.seq.value;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.id.domain.seq.SequenceConfig;

public class SequenceProviderFactoryHolderTest {

    private final SequenceProviderFactoryHolder holder = new SequenceProviderFactoryHolder();

    @Test
    public void nextWithDefaultConfig() {
        Assert.assertEquals(1, next(null).intValue());
        Assert.assertEquals(2, next(null).intValue());
    }

    @Test
    public void nextWithDefaultCustom() {
        String config = "{\n" +
                "  \"step\": 2" +
                "}";

        Assert.assertEquals(1, next(config).intValue());
        Assert.assertEquals(3, next(config).intValue());
    }

    private Number next(String config) {
        SequenceConfig c = new SequenceConfig();
        c.setSequenceFactoryId("MS");
        c.setSequenceConfig(config);
        return holder.provide(new SequenceProvider.Context(c, "TEST", null));
    }
}