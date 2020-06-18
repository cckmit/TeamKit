package org.team4u.ddd.domain.model;


import cn.hutool.core.lang.Assert;

import java.io.Serializable;

public abstract class AbstractId implements Identity, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    protected AbstractId(String anId) {
        this();

        this.setId(anId);
    }

    protected AbstractId() {
        super();
    }

    public String id() {
        return this.id;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            AbstractId typedObject = (AbstractId) anObject;
            equalObjects = this.id().equals(typedObject.id());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        return +(this.hashOddValue() * this.hashPrimeValue())
                + this.id().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [id=" + id + "]";
    }

    protected abstract int hashOddValue();

    protected abstract int hashPrimeValue();

    protected void validateId(String id) {
        // implemented by subclasses for validation.
        // throws a runtime exception if invalid.
    }

    private void setId(String id) {
        Assert.notEmpty(id, "The basic identity is required.");

        this.validateId(id);

        this.id = id;
    }
}