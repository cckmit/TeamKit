package org.team4u.kv.resource;

import java.util.Objects;

/**
 * 存储资源
 */
public class StoreResource {

    /**
     * 资源标识
     */
    private String id;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 资源名称
     */
    private String name;

    public StoreResource(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreResource resource = (StoreResource) o;
        return Objects.equals(id, resource.id) &&
                Objects.equals(type, resource.type) &&
                Objects.equals(name, resource.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name);
    }

    @Override
    public String toString() {
        return type + '/' + id + '/' + name;
    }
}