package org.team4u.kit.core.action;

public interface Function<K, V> {

    V invoke(K obj);
}