package org.team4u.config.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.message.AbstractMessageSubscriber;
import org.team4u.base.message.MessagePublisher;
import org.team4u.config.domain.event.ConfigAllChangedEvent;
import org.team4u.config.domain.event.ConfigDisabledEvent;
import org.team4u.config.domain.event.ConfigValueChangedEvent;
import org.team4u.config.domain.repository.CacheableSimpleConfigRepository;

import java.util.List;

import static org.team4u.config.TestUtil.c;

public class CacheableSimpleConfigRepositoryTest {

    private final ConfigEventConsumer configEventConsumer = new ConfigEventConsumer();

    public CacheableSimpleConfigRepositoryTest() {
        MessagePublisher.instance().subscribe(configEventConsumer);
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
        Assert.assertEquals(ConfigAllChangedEvent.class, configEventConsumer.getEvent().getClass());

        ConfigDisabledEvent disabledEvent = (ConfigDisabledEvent) configEventConsumer.getEvent().getAllEvents().get(0);
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
        ConfigAllChangedEvent configAllChangedEvent = configEventConsumer.getEvent();
        ConfigValueChangedEvent changedEvent = (ConfigValueChangedEvent) configAllChangedEvent.getAllEvents().get(0);
        Assert.assertEquals("1", changedEvent.getOldValue());
        Assert.assertEquals("2", changedEvent.getNewValue());
    }

    private static class ConfigEventConsumer extends AbstractMessageSubscriber<ConfigAllChangedEvent> {

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