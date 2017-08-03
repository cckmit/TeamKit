package org.team4u.kit.core.util;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.util.ClassUtil;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class ServiceProvider implements Closeable {

    private static final Log log = LogFactory.get();

    private static final ServiceProvider DEFAULT = new ServiceProvider();

    private Map<String, Object> services = new LinkedHashMap<String, Object>();

    public static ServiceProvider getInstance() {
        return DEFAULT;
    }

    public <T> T get(Class<T> keyClass) {
        return get(keyClass.getSimpleName());
    }

    public <T> T getOrRegister(Class<T> keyClass) {
        T instance = get(keyClass);
        if (instance != null) {
            return instance;
        }

        synchronized (this) {
            instance = get(keyClass);
            if (instance != null) {
                return instance;
            }

            instance = ClassUtil.newInstance(keyClass);
            register(instance);
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object object = services.get(key);

        AssertUtil.notNull(key, "Service not fount(key=%s)", key);

        if (object instanceof Factory) {
            Factory<T> factory = (Factory<T>) object;
            T instance;

            switch (factory.getScope()) {
                case prototype:
                    log.debug("Creating new service(key=%s)", key);
                    instance = factory.create();
                    log.debug("Created new service(key=%s,instance=%s)", key, instance);
                    break;

                default:
                    synchronized (this) {
                        if (services.get(key) instanceof Factory) {
                            log.debug("Creating new service(key=%s)", key);
                            instance = factory.create();
                            log.debug("Created new service(key=%s,instance=%s)", key, instance);

                            register(key, instance);
                        } else {
                            instance = get(key);
                        }
                    }
            }

            return instance;
        } else {
            log.trace("Get service(key=%s,instance=%s)", key, object);
            return (T) object;
        }
    }

    public ServiceProvider register(String key, Object service) {
        log.debug("Register service(key=%s,instance=%s)", key, service);
        services.put(key, service);
        return this;
    }

    public <T> ServiceProvider register(Class<?> keyClass, T service) {
        return register(keyClass.getSimpleName(), service);
    }

    public <T> ServiceProvider register(T service) {
        return register(service.getClass(), service);
    }

    @Override
    public void close() throws IOException {
        ArrayList<Map.Entry<String, Object>> entries = new ArrayList<Map.Entry<String, Object>>(services.entrySet());
        Collections.reverse(entries);

        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof Closeable) {
                try {
                    ((Closeable) entry.getValue()).close();
                    log.debug("Service closed(key=%s,instance=%s)", entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    log.error(String.format("Close service failure(key=%s,instance=%s)",
                            entry.getKey(), entry.getValue()), e);
                }
            }
        }
    }

    public enum Scope {
        singleton, prototype
    }

    public static abstract class Factory<T> {

        public abstract T create();

        public Scope getScope() {
            return Scope.singleton;
        }
    }
}