package org.team4u.config.domain;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.domain.event.*;

import java.util.List;

import static org.team4u.config.TestUtil.c;

public class SimpleConfigComparatorTest {

    private final SimpleConfigComparator c = new SimpleConfigComparator();

    @Test
    public void create() {
        List<SimpleConfig> newConfigs = CollUtil.newArrayList(c("a", "1"));
        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(CollUtil.newArrayList(), newConfigs);
        Assert.assertEquals(1, events.size());
        ConfigCreatedEvent event = (ConfigCreatedEvent) events.get(0);
        Assert.assertEquals("1", event.getNewValue());
        Assert.assertNull(event.getOldValue());
        Assert.assertEquals(event.getNewValue(), event.getSimpleConfig().getConfigValue());
    }

    @Test
    public void disable() {
        List<SimpleConfig> oldConfigs = CollUtil.newArrayList(c("a", "1"));
        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(oldConfigs, CollUtil.newArrayList());
        Assert.assertEquals(1, events.size());
        ConfigDisabledEvent event = (ConfigDisabledEvent) events.get(0);
        Assert.assertEquals("1", event.getOldValue());
        Assert.assertNull(event.getNewValue());
    }

    @Test
    public void changeValue() {
        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(
                CollUtil.newArrayList(c("a", "1")),
                CollUtil.newArrayList(c("a", "2"))
        );
        Assert.assertEquals(1, events.size());
        ConfigValueChangedEvent event = (ConfigValueChangedEvent) events.get(0);
        Assert.assertEquals("1", event.getOldValue());
        Assert.assertEquals("2", event.getNewValue());
    }

    @Test
    public void changeDesc() {
        SimpleConfig n = c("a", "1");
        n.setDescription("1");

        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(
                CollUtil.newArrayList(c("a", "1")),
                CollUtil.newArrayList(n)
        );
        Assert.assertEquals(1, events.size());
        ConfigDescChangedEvent event = (ConfigDescChangedEvent) events.get(0);
        Assert.assertNull(event.getOldValue());
        Assert.assertEquals("1", event.getNewValue());
    }

    @Test
    public void changeSeq() {
        SimpleConfig n = c("a", "1");
        n.setSequenceNo(1);

        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(
                CollUtil.newArrayList(c("a", "1")),
                CollUtil.newArrayList(n)
        );
        Assert.assertEquals(1, events.size());
        ConfigSequenceNoChangedEvent event = (ConfigSequenceNoChangedEvent) events.get(0);
        Assert.assertEquals(0, event.getOldValue().intValue());
        Assert.assertEquals(1, event.getNewValue().intValue());
    }

    @Test
    public void enable() {
        SimpleConfig n = c("a", "1");
        n.setEnabled(false);

        List<SimpleConfigEvent> events = new SimpleConfigComparator().compare(
                CollUtil.newArrayList(n),
                CollUtil.newArrayList(c("a", "1"))
        );
        Assert.assertEquals(1, events.size());
        ConfigEnabledEvent event = (ConfigEnabledEvent) events.get(0);
        Assert.assertNull(event.getOldValue());
        Assert.assertEquals("1", event.getNewValue());
    }
}