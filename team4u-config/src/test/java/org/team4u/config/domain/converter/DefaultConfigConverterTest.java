package org.team4u.config.domain.converter;

import org.team4u.config.domain.SimpleConfigConverter;

public class DefaultConfigConverterTest extends AbstractConfigConverterTest {

    @Override
    protected SimpleConfigConverter newSimpleConfigConverter() {
        return new DefaultConfigConverter(repository);
    }
}