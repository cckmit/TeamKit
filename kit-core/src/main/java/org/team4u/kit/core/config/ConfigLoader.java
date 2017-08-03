package org.team4u.kit.core.config;

import org.team4u.kit.core.lang.Registry;

public interface ConfigLoader<V> extends Registry.Applicant<String> {

    <T> T load(Class<T> configClass, V content);
}