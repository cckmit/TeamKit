package org.team4u.config.application;

public class DefaultConfigConverterTest extends AbstractConfigConverterTest {

    private final MockSimpleConfigRepository mockRepository = new MockSimpleConfigRepository();

    private final DefaultConfigConverter converter = new DefaultConfigConverter(mockRepository);

    @Override
    protected SimpleConfigConverter converter() {
        return converter;
    }

    @Override
    protected MockSimpleConfigRepository mockRepository() {
        return mockRepository;
    }
}