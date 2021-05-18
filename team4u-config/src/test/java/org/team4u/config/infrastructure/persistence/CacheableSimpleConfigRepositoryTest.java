package org.team4u.config.infrastructure.persistence;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.event.ConfigDeletedEvent;
import org.team4u.config.domain.event.ConfigValueChangedEvent;
import org.team4u.ddd.domain.model.AbstractDomainEvent;
import org.team4u.ddd.domain.model.DomainEventPublisher;
import org.team4u.ddd.message.AbstractMessageConsumer;

import java.util.List;

import static org.team4u.config.TestUtil.c;

public class CacheableSimpleConfigRepositoryTest {

    private final ConfigEventConsumer configEventConsumer = new ConfigEventConsumer();

    public CacheableSimpleConfigRepositoryTest() {
        DomainEventPublisher.instance().subscribe(configEventConsumer);
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
        Assert.assertEquals(ConfigDeletedEvent.class, configEventConsumer.getEvent().getClass());
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
        ConfigValueChangedEvent event = (ConfigValueChangedEvent) configEventConsumer.getEvent();
        Assert.assertEquals("1", event.getOldValue());
        Assert.assertEquals("2", event.getNewValue());
    }

    private static class ConfigEventConsumer extends AbstractMessageConsumer<AbstractDomainEvent> {

        private AbstractDomainEvent event;

        @Override
        protected void internalProcessMessage(AbstractDomainEvent message) throws Throwable {
            event = message;
        }

        public AbstractDomainEvent getEvent() {
            return event;
        }
    }

    private static class MockSimpleConfigRepository implements SimpleConfigRepository {

        private List<SimpleConfig> configs;

        @Override
        public List<SimpleConfig> allConfigs() {
            return configs;
        }

        public List<SimpleConfig> getConfigs() {
            return configs;
        }

        public void setConfigs(List<SimpleConfig> configs) {
            this.configs = configs;
        }
    }
}