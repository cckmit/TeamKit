package org.team4u.exporter.domain;

public class Title {

    private final String key;

    private final String name;

    public Title(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}