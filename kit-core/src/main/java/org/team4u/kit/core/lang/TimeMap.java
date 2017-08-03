package org.team4u.kit.core.lang;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TimeMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_NUM_BUCKETS = 3;
    private final Object _lock = new Object();
    private LinkedList<Map<K, V>> _buckets;
    private Thread _cleaner;
    private ExpiredCallback<K, V> _callback;
    private long valueTimeToLive;

    public TimeMap(long expirationMillis, int numBuckets, ExpiredCallback<K, V> callback) {
        if (numBuckets < 2) {
            throw new IllegalArgumentException("numBuckets must be >= 2");
        }

        this.valueTimeToLive = expirationMillis;
        _buckets = new LinkedList<Map<K, V>>();
        for (int i = 0; i < numBuckets; i++) {
            _buckets.add(new ConcurrentHashMap<K, V>());
        }

        _callback = callback;
        final long sleepTime = expirationMillis / (numBuckets - 1);

        _cleaner = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Map<K, V> dead;
                        Thread.sleep(sleepTime);

                        synchronized (_lock) {
                            //删掉最后一个桶，在头补充一个新的桶，最后一个桶的数据是最旧的
                            dead = _buckets.removeLast();
                            _buckets.addFirst(new ConcurrentHashMap<K, V>());
                        }
                        if (_callback != null) {
                            for (Map.Entry<K, V> entry : dead.entrySet()) {
                                _callback.expire(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    // Ignore error
                }
            }
        });

        _cleaner.setDaemon(true);
        _cleaner.start();
    }

    public TimeMap(long valueTimeToLive, ExpiredCallback<K, V> callback) {
        this(valueTimeToLive, DEFAULT_NUM_BUCKETS, callback);
    }

    public TimeMap(long valueTimeToLive) {
        this(valueTimeToLive, DEFAULT_NUM_BUCKETS);
    }

    public TimeMap(long valueTimeToLive, int numBuckets) {
        this(valueTimeToLive, numBuckets, null);
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                if (bucket.containsKey(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public V get(Object key) {
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                //noinspection SuspiciousMethodCalls
                if (bucket.containsKey(key)) {
                    return bucket.get(key);
                }
            }

            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        synchronized (_lock) {
            Iterator<Map<K, V>> it = _buckets.iterator();
            Map<K, V> bucket = it.next();
            //在第一个桶上更新数据
            bucket.put(key, value);
            //去掉后面桶的数据
            while (it.hasNext()) {
                bucket = it.next();
                bucket.remove(key);
            }
        }

        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                //noinspection SuspiciousMethodCalls
                if (bucket.containsKey(key)) {
                    return bucket.remove(key);
                }
            }

            return null;
        }
    }

    public int size() {
        synchronized (_lock) {
            int size = 0;
            for (Map<K, V> bucket : _buckets) {
                size += bucket.size();
            }

            return size;
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public void neverCleanup() {
        _cleaner.interrupt();
    }

    @Override
    public void clear() {
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                bucket.clear();
            }
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<K>();
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                result.addAll(bucket.keySet());
            }

            return result;
        }
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<V>();
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                result.addAll(bucket.values());
            }

            return result;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
        synchronized (_lock) {
            for (Map<K, V> bucket : _buckets) {
                result.addAll(bucket.entrySet());
            }

            return result;
        }
    }

    public interface ExpiredCallback<K, V> {
        void expire(K key, V val);
    }
}