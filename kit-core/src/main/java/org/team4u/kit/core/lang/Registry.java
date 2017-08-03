package org.team4u.kit.core.lang;


import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Registry<K, V extends Registry.Applicant<K>> {

    protected Map<K, V> applicants = new ConcurrentHashMap<K, V>();

    public Registry<K, V> register(K key, V value) {
        applicants.put(key, value);
        return this;
    }

    public Registry<K, V> register(V value) {
        applicants.put(value.getKey(), value);
        return this;
    }

    public Registry<K, V> unregister(K key) {
        applicants.remove(key);
        return this;
    }

    public Registry<K, V> unregister(V value) {
        unregister(value.getKey());
        return this;
    }

    public void unregisterAll() {
        applicants.clear();
    }

    public V get(K key) {
        return applicants.get(key);
    }

    public boolean containsKey(K key) {
        return applicants.containsKey(key);
    }

    public Collection<V> registers() {
        return applicants.values();
    }

    public interface Applicant<K> {

        K getKey();
    }
}