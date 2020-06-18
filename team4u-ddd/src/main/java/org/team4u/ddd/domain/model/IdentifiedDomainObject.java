package org.team4u.ddd.domain.model;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.Serializable;

public class IdentifiedDomainObject implements Serializable {

    private static final long serialVersionUID = 1L;
    protected Log log = LogFactory.get();
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}