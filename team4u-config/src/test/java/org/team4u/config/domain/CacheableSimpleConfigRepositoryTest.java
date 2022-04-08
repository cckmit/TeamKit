package org.team4u.config.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.message.jvm.AbstractMessageSubscriber;
import org.team4u.base.message.jvm.MessagePublisher;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.config.domain.event.ConfigDisabledEvent;
import org.team4u.config.domain.event.ConfigValueChangedEvent;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;

import java.util.List;

import static org.team4u.config.TestUtil.c;

public class CacheableSimpleConfigRepositoryTest {

    private final ConfigEventSubscriber configEventSubscriber = new ConfigEventSubscriber();

    public CacheableSimpleConfigRepositoryTest() {
        MessagePublisher.instance().subscribe(configEventSubscriber);
    }

    @Test
    public void create() {
        MockSimpleConfigRepository mockSimpleConfigRepository = new MockSimpleConfigRepository();
        mockSimpleConfigRepository.setConfigs(CollUtil.newArrayList(c("a", "1")));
        CacheableSimpleConfigRepository repository = new CacheableSimpleConfigRepository(
                new CacheableSimpleConfigRepository.Config().setMaxEffectiveSec(10),
                mockSimpleConfigRepository
        );

        Assert.assertEquals(1, repository.allConfigs().size());
        Assert.assertEquals(repository.allConfigs(), repository.allConfigs());
        Assert.assertNull(configEventSubscriber.getEvent());
    }

    @Test
    public void delete() {
        MockSimpleConfigRepository mockSimpleConfigRepository = new MockSimpleConfigRepository();
        mockSimpleConfigRepository.setConfigs(CollUtil.newArrayList(c("a", "1")));
        CacheableSimpleConfigRepository repository = new CacheableSimpleConfigRepository(
                new CacheableSimpleConfigRepository.Config().setMaxEffectiveSec(1),
                mockSimpleConfigRepository
        );

        Assert.assertEquals(1, repository.allConfigs().size());

        mockSimpleConfigRepository.setConfigs(CollUtil.newArrayList());
        ThreadUtil.sleep(1500);

        Assert.assertEquals(0, repository.allConfigs().size());
        Assert.assertEquals(ConfigAllChangedEvent.class, configEventSubscriber.getEvent().getClass());

        ConfigDisabledEvent disabledEvent = (ConfigDisabledEvent) configEventSubscriber.getEvent().getAllEvents().get(0);
        Assert.assertNull(disabledEvent.getNewValue());
    }

    @Test
    public void valueChanged() {
        MockSimpleConfigRepository mockSimpleConfigRepository = new MockSimpleConfigRepository();
        mockSimpleConfigRepository.setConfigs(CollUtil.newArrayList(c("a", "1")));
        CacheableSimpleConfigRepository repository = new CacheableSimpleConfigRepository(
                new CacheableSimpleConfigRepository.Config().setMaxEffectiveSec(1),
                mockSimpleConfigRepository
        );

        Assert.assertEquals(1, repository.allConfigs().size());
        mockSimpleConfigRepository.setConfigs(CollUtil.newArrayList(c("a", "2")));
        ThreadUtil.sleep(1500);

        Assert.assertEquals(1, repository.allConfigs().size());
        ConfigAllChangedEvent configAllChangedEvent = configEventSubscriber.getEvent();
        ConfigValueChangedEvent changedEvent = (ConfigValueChangedEvent) configAllChangedEvent.getAllEvents().get(0);
        Assert.assertEquals("1", changedEvent.getOldValue());
        Assert.assertEquals("2", changedEvent.getNewValue());
    }

    private static class ConfigEventSubscriber extends AbstractMessageSubscriber<ConfigAllChangedEvent> {

        private ConfigAllChangedEvent event;

        @Override
        protected void internalOnMessage(ConfigAllChangedEvent message) {
            event = message;
        }

        public ConfigAllChangedEvent getEvent() {
            return event;
        }
    }

    private static class MockSimpleConfigRepository implements SimpleConfigRepository {

        private List<SimpleConfig> configs;

        @Override
        public SimpleConfigs allConfigs() {
            return new SimpleConfigs(configs);
        }

        public void setConfigs(List<SimpleConfig> configs) {
            this.configs = configs;
        }
    }
}