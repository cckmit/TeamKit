package org.team4u.base.log.audit.domain;

import lombok.Data;

@Data
public class Operator {

    public static final Operator SYSTEM = new Operator("SYSTEM");

    private final String id;
    private final String name;

    public Operator(String id) {
        this(id, id);
    }

    public Operator(String id, String name) {
        this.id = id;
        this.name = name;
    }
}