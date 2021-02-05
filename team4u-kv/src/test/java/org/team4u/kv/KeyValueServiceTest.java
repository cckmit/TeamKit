package org.team4u.kv;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class KeyValueServiceTest {

    @Test
    @Transactional(value = "txManager")
    public void putAndGet() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        Assert.assertEquals("1", s.get("x"));
        Assert.assertEquals(0, s.expirationTimestamp("x"));

        s.put("x", "2");
        Assert.assertEquals("2", s.get("x"));
    }

    @Test
    @Transactional(value = "txManager")
    public void size() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        s.put("y", "1");
        Assert.assertEquals(2, s.size());
    }

    @Test
    @Transactional(value = "txManager")
    public void clear() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        s.put("y", "1");
        s.clear();

        Assert.assertEquals(0, s.size());
        Assert.assertTrue(s.isEmpty());
    }

    @Test
    @Transactional(value = "txManager")
    public void containsKey() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");

        Assert.assertTrue(s.containsKey("x"));
        Assert.assertFalse(s.containsKey("y"));
    }

    @Test
    @Transactional(value = "txManager")
    public void putIfAbsent() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        Assert.assertEquals("1", s.get("x"));
        Assert.assertNull(s.putIfAbsent("x", "2"));
        Assert.assertEquals("1", s.get("x"));
    }

    @Test
    @Transactional(value = "txManager")
    public void expirationWithClean() {
        testForExpirationKv(500, true);
    }

    @Test
    @Transactional(value = "txManager")
    public void expirationWithoutClean() {
        testForExpirationKv(500, false);
    }

    private void testForExpirationKv(int ttlMillis, boolean shouldClean) {
        SimpleKeyValueService<String> s = newKeyValueService(ttlMillis, 0);
        s.put("x", "1");
        s.put("y", "2");

        Assert.assertTrue(s.expirationTimestamp("x") - System.currentTimeMillis() > 0);
        Assert.assertTrue(s.expirationTimestamp("y") - System.currentTimeMillis() > 0);

        Assert.assertEquals("1", s.get("x"));
        Assert.assertEquals("2", s.get("y"));

        ThreadUtil.sleep(ttlMillis);

        if (shouldClean) {
            keyValueRepository().removeExpirationValues(100);
        }

        Assert.assertNull(s.get("x"));
        Assert.assertNull(s.get("y"));
        Assert.assertEquals(-1, s.expirationTimestamp("x"));
        Assert.assertEquals(-1, s.expirationTimestamp("y"));

        s.put("x", "1");
        s.put("y", "2");
        Assert.assertEquals("1", s.get("x"));
        Assert.assertEquals("2", s.get("y"));
    }

    @Test
    @Transactional(value = "txManager")
    public void putAll() {
        Map<String, String> data = new HashMap<>();
        data.put("x", "1");

        SimpleKeyValueService<String> s = newKeyValueService();
        s.putAll(data);

        Assert.assertEquals("1", s.get("x"));
    }

    @Test
    @Transactional(value = "txManager")
    public void values() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        Collection<String> values = s.values();
        Assert.assertEquals(1, values.size());
        Assert.assertTrue(values.contains("1"));
    }

    @Test
    @Transactional(value = "txManager")
    public void entrySet() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        Set<KeyValueService.KeyValueEntry<String>> entries = s.entrySet();
        Assert.assertEquals(1, entries.size());
        KeyValueService.KeyValueEntry entry = CollUtil.getFirst(entries);
        Assert.assertEquals("x", entry.getKey());
        Assert.assertEquals("1", entry.getValue());
    }

    @Test
    @Transactional(value = "txManager")
    public void keySet() {
        SimpleKeyValueService<String> s = newKeyValueService();
        s.put("x", "1");
        Set<String> keySet = s.keySet();
        Assert.assertEquals(1, keySet.size());
        String key = CollUtil.getFirst(keySet);
        Assert.assertEquals("x", key);
    }

    @Test
    @Transactional(value = "txManager")
    public void waitingForGet() throws TimeoutException {
        KeyValueService s = mockKeyValueServiceForWaitingGet(3);
        Assert.assertEquals("1", s.get("", "", String.class, 10, 20));
    }

    @Test
    @Transactional(value = "txManager")
    public void waitingTimeoutForGet() {
        KeyValueService s = mockKeyValueServiceForWaitingGet(4);

        try {
            s.get("", "", String.class, 10, 20);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(TimeoutException.class, e.getClass());
        }
    }

    private KeyValueService mockKeyValueServiceForWaitingGet(int waitingCount) {
        AtomicInteger count = new AtomicInteger();
        return new KeyValueService(null, null) {
            @Override
            public <V> V get(String type, String key, Class<V> valueClass) {
                int x = count.incrementAndGet();

                if (x == waitingCount) {
                    return valueTranslator().translateToValue(valueClass, "1");
                }

                return null;
            }
        };
    }

    private SimpleKeyValueService<String> newKeyValueService() {
        return newKeyValueService(0, 0);
    }

    private SimpleKeyValueService<String> newKeyValueService(int ttlMillis, int cleanerIntervalMillis) {
        KeyValueCleaner cleaner = null;
        if (ttlMillis > 0) {
            cleaner = new KeyValueCleaner(
                    new KeyValueCleaner.Config()
                            .setIntervalMillis(cleanerIntervalMillis)
                            .setMaxBatchSize(10),
                    null
            );
        }

        return new SimpleKeyValueService<>(
                "TEST",
                ttlMillis,
                String.class,
                keyValueRepository(),
                cleaner
        );
    }

    protected abstract KeyValueRepository keyValueRepository();
}