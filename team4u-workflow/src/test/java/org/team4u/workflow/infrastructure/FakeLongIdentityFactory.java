package org.team4u.workflow.infrastructure;


import org.team4u.base.id.IdentityFactory;

public class FakeLongIdentityFactory implements IdentityFactory<Long> {

    private static final FakeLongIdentityFactory instance = new FakeLongIdentityFactory();
    private long id = 0;

    public static FakeLongIdentityFactory getInstance() {
        return instance;
    }

    @Override
    public Long create() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public FakeLongIdentityFactory setId(Long id) {
        this.id = id;
        return this;
    }
}