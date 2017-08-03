package org.team4u.kit.core.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil {

    private static ThreadLocal<Map<Object, Object>> local = new ThreadLocal<Map<Object, Object>>();

    static {
        local.set(new HashMap<Object, Object>());
    }

    public static void set(Object key, Object value) {
        local.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object key) {
        return (T) local.get().get(key);
    }
}