package org.team4u.base.serializer;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.serializer.json.AbstractCacheSerializerTest;

public class SmartCacheSerializerTest extends AbstractCacheSerializerTest {

    @Test
    public void simple() {
        Assert.assertTrue(SmartCacheSerializer.getInstance().deserialize("1", Boolean.class));
    }

    @Override
    protected Serializer serializer() {
        return SmartCacheSerializer.getInstance();
    }
}