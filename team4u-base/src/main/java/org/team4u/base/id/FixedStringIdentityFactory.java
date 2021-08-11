package org.team4u.base.id;

/**
 * 固定值主键生成器
 *
 * @author jay.wu
 */
public class FixedStringIdentityFactory implements StringIdentityFactory {

    private final String id;

    public FixedStringIdentityFactory(String id) {
        this.id = id;
    }

    @Override
    public String create() {
        return id;
    }
}