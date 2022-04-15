package org.team4u.config.domain.converter;

import org.team4u.config.domain.SimpleConfigConverter;

public class DefaultConfigConverterTest extends AbstractConfigConverterTest {

    private final DefaultConfigConverter converter = new DefaultConfigConverter(new MockSimpleConfigRepository());

    @Override
    protected SimpleConfigConverter converter() {
        return converter;
    }
}