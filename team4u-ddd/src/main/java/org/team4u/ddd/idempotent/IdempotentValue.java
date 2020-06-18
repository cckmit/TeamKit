package org.team4u.ddd.idempotent;

import org.team4u.ddd.domain.model.IdentifiedValueObject;

public class IdempotentValue extends IdentifiedValueObject {

    private String idempotentId;

    private String typeName;

    public IdempotentValue(String idempotentId, String typeName) {
        this.idempotentId = idempotentId;
        this.typeName = typeName;
    }

    public String getIdempotentId() {
        return idempotentId;
    }

    public String getTypeName() {
        return typeName;
    }
}