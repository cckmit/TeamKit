package org.team4u.config.domain;

public interface SimpleConfigRepository {

    SimpleConfig configOf(String configType,String configKey);
}