package org.team4u.ddd.specification;

import cn.hutool.core.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;

public class SpecificationConverterService {

    private Map<Class<?>, SpecificationConverter<?, ?, ?, ?>> converterMapping = new HashMap<>();

    public void registerConverter(SpecificationConverter<?, ?, ?, ?> converter) {
        Class<?> specificationClass = ClassUtil.getTypeArgument(converter.getClass());
        converterMapping.put(specificationClass, converter);
    }

    @SuppressWarnings("unchecked")
    public <T, S extends Specification<T>> SpecificationConverter<S, T, ?, ?> findConverter(S specification) {
        Class<?> specificationClass = specification.getClass();
        Object converter;
        do {
            converter = converterMapping.get(specificationClass);
        } while (converter == null && (specificationClass = specificationClass.getSuperclass()) != null);
        return (SpecificationConverter<S, T, ?, ?>) converter;
    }

    @SuppressWarnings("unchecked")
    public <T, C, V> V translateToPredicate(Specification<T> specification, C context) {
        SpecificationConverter<Specification<T>, T, C, V> converter =
                (SpecificationConverter<Specification<T>, T, C, V>) findConverter(specification);

        if (converter == null) {
            String message = String.format("Specification [%s] has no registered converters.",
                    specification.getClass().getName());
            throw new IllegalArgumentException(message);
        }
        return converter.convert(specification, context);
    }
}