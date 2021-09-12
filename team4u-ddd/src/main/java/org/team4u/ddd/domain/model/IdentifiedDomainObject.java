package org.team4u.ddd.domain.model;

import java.io.Serializable;

public class IdentifiedDomainObject implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}