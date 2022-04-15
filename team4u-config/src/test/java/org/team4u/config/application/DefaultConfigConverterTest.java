package org.team4u.config.application;

public class DefaultConfigConverterTest extends AbstractConfigConverterTest {

    private final DefaultConfigConverter converter = new DefaultConfigConverter(new MockSimpleConfigRepository());

    @Override
    protected SimpleConfigConverter converter() {
        return converter;
    }
}