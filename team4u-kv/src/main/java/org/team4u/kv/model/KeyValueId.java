package org.team4u.kv.model;

import java.beans.Transient;

/**
 * 键值标识
 */
public class KeyValueId {
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;

    public KeyValueId(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static KeyValueId of(String type, String name) {
        return new KeyValueId(type, name);
    }

    @Transient
    public KeyValueId id() {
        return of(getType(), getName());
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return type + ":" + name;
    }
}